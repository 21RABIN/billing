package com.rbilling.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rbilling.model.BusinessUnit;
import com.rbilling.model.Employee;
import com.rbilling.repository.BusinessUnitRepository;
import com.rbilling.repository.EmployeeRepository;
import com.rbilling.repository.RoleRepository;

@Service
public class AccessScopeService {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private BusinessUnitRepository businessUnitRepository;

	public String getRole(Long userId) {
		if (userId == null) {
			return "";
		}
		String role = roleRepository.getRoleuserid(userId);
		return role == null ? "" : role.trim().toUpperCase(Locale.ROOT);
	}

	public boolean isAdmin(Long userId) {
		return "ROLE_ADMIN".equals(getRole(userId));
	}

	public Long getEmployeeId(Long userId) {
		if (userId == null) {
			return null;
		}
		Employee employee = employeeRepository.findByUserId(userId);
		return employee == null ? null : employee.getId();
	}

	public List<Long> getAccessibleBusinessUnitIds(Long userId) {
		if (userId == null) {
			return Collections.emptyList();
		}

		String role = getRole(userId);

		if ("ROLE_ADMIN".equals(role)) {
			List<Long> ids = new ArrayList<>();
			for (BusinessUnit unit : businessUnitRepository.findAll()) {
				if (unit != null && unit.getId() != null) {
					ids.add(unit.getId());
				}
			}
			return ids;
		}

		if ("ROLE_EMPLOYEE".equals(role) || "ROLE_MANAGER".equals(role)) {
			Employee employee = employeeRepository.findByUserId(userId);
			if (employee == null || employee.getBusiness_unit_id() == null) {
				return Collections.emptyList();
			}

			if ("ROLE_EMPLOYEE".equals(role)) {
				List<Long> ids = new ArrayList<>();
				ids.add(employee.getBusiness_unit_id());
				return ids;
			}

			List<Long> ids = businessUnitRepository.getAllChildUnitIds(employee.getBusiness_unit_id());
			return ids == null ? Collections.emptyList() : ids;
		}

		if ("ROLE_MAIN".equals(role) || "ROLE_FRANCHESE".equals(role)) {
			BusinessUnit unit = businessUnitRepository.findByUserId(userId);
			if (unit == null || unit.getId() == null) {
				return Collections.emptyList();
			}
			List<Long> ids = businessUnitRepository.getAllChildUnitIds(unit.getId());
			return ids == null ? Collections.emptyList() : ids;
		}

		return Collections.emptyList();
	}

	public List<Map<String, Object>> filterRowsByBusinessUnits(List<Map<String, Object>> rows, String key,
			Long userId) {
		if (rows == null || rows.isEmpty()) {
			return Collections.emptyList();
		}

		if (isAdmin(userId)) {
			return rows;
		}

		List<Long> unitIds = getAccessibleBusinessUnitIds(userId);
		if (unitIds.isEmpty()) {
			return Collections.emptyList();
		}

		Set<Long> allowed = new HashSet<>(unitIds);
		List<Map<String, Object>> filtered = new ArrayList<>();

		for (Map<String, Object> row : rows) {
			Object value = row.get(key);
			Long rowUnitId = toLong(value);
			if (rowUnitId != null && allowed.contains(rowUnitId)) {
				filtered.add(row);
			}
		}
		return filtered;
	}

	private Long toLong(Object value) {
		if (value == null) {
			return null;
		}
		if (value instanceof Number) {
			return ((Number) value).longValue();
		}
		try {
			return Long.parseLong(value.toString());
		} catch (Exception ex) {
			return null;
		}
	}
}
