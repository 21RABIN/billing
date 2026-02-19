package com.rbilling.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rbilling.DTO.RetailInvoiceRequestDTO;
import com.rbilling.model.Customer;
import com.rbilling.model.Invoice;
import com.rbilling.model.InvoiceItem;
import com.rbilling.model.Payment;
import com.rbilling.model.Product;
import com.rbilling.model.ProductBatch;
import com.rbilling.repository.CustomerRepository;
import com.rbilling.repository.InvoiceItemRepository;
import com.rbilling.repository.InvoiceRepository;
import com.rbilling.repository.PaymentRepository;
import com.rbilling.repository.ProductBatchRepository;
import com.rbilling.repository.ProductRepository;
import com.rbilling.service.RetailBillingService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RetailBillingServiceImpl implements RetailBillingService {

	@Autowired
	ProductRepository productRepo;
	@Autowired
	ProductBatchRepository batchRepo;
	@Autowired
    InvoiceRepository invoiceRepo;
	@Autowired
    InvoiceItemRepository itemRepo;
	@Autowired
    PaymentRepository paymentRepo;
    @Autowired
    private CustomerRepository cusrepo;

    public String createRetailInvoice(RetailInvoiceRequestDTO request) {

        BigDecimal totalTax = BigDecimal.ZERO;
        BigDecimal grossAmount = BigDecimal.ZERO;
        
        Optional<Customer> cus=cusrepo.findById(request.getCustomer_id());

        // ✅ Create Invoice
        Invoice invoice = new Invoice();
        invoice.setBusiness_unit_id(cus.get().getBusiness_unit_id());
        invoice.setCustomer_id(request.getCustomer_id());
        invoice.setInvoice_date(LocalDate.now());
        invoice.setStatus("ESTIMATE");
        invoice.setBilled_by(request.getBilled_by());

        invoice = invoiceRepo.save(invoice);

        // ✅ Loop Products
        for (RetailInvoiceRequestDTO.ProductItemDTO item : request.getProductitems()) {

            Product product = productRepo.findById(item.getProduct_id())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            BigDecimal price = product.getSelling_price() != null
                    ? product.getSelling_price()
                    : BigDecimal.ZERO;

            BigDecimal gstPercent = product.getGst_percent() != null
                    ? product.getGst_percent()
                    : BigDecimal.ZERO;

            BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());

            BigDecimal lineAmount = price.multiply(quantity);

            BigDecimal tax = lineAmount
                    .multiply(gstPercent)
                    .divide(BigDecimal.valueOf(100));
            
            

            BigDecimal total = lineAmount.add(tax);
            

            // ✅ Batch Handling
            if (Boolean.TRUE.equals(product.getTrack_batch())) {

                if (item.getBatch_id() == null) {
                    throw new RuntimeException("Batch required for this product");
                }

                ProductBatch batch = batchRepo.findById(item.getBatch_id())
                        .orElseThrow(() -> new RuntimeException("Batch not found"));

                if (batch.getStock_qty() < item.getQuantity()) {
                    throw new RuntimeException("Insufficient stock");
                }

                batch.setStock_qty(batch.getStock_qty() - item.getQuantity());
                batchRepo.save(batch);
            }

            // ✅ Save Invoice Item
            InvoiceItem invoiceItem = new InvoiceItem();
            invoiceItem.setInvoice_id(invoice.getId());
            invoiceItem.setItem_type("PRODUCT");
            invoiceItem.setItem_id(product.getId());
            invoiceItem.setDescription(product.getName());
            invoiceItem.setQuantity(item.getQuantity());
            invoiceItem.setGst_percent(gstPercent);
            invoiceItem.setTax_amount(tax);
            invoiceItem.setTotal_amount(total);

            itemRepo.save(invoiceItem);

            grossAmount = grossAmount.add(lineAmount);
            totalTax = totalTax.add(tax);
        }

        // ✅ Final Invoice Calculation
        BigDecimal netAmount = grossAmount.add(totalTax);

        invoice.setGross_amount(grossAmount);
        invoice.setTax_amount(totalTax);
        invoice.setTotal_amount(netAmount);
        invoice.setNet_amount(netAmount);
        invoice.setRound_off(BigDecimal.ZERO);

        // ✅ Payment Handling
        if (request.getPayment() != null) {

            Payment payment = new Payment();
            payment.setInvoice_id(invoice.getId());
            payment.setAmount(request.getPayment().getAmount());
            payment.setMode(request.getPayment().getMode());
            payment.setReference_no(request.getPayment().getReference_no());
            payment.setPayment_date(LocalDate.now());

            paymentRepo.save(payment);

            if (request.getPayment().getAmount().compareTo(netAmount) >= 0) {
                invoice.setStatus("PAID");
            } else {
                invoice.setStatus("PARTIAL");
            }
        }

        invoiceRepo.save(invoice);

        return "Retail Invoice Created Successfully";
    }
}
