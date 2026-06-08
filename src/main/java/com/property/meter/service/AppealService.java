package com.property.meter.service;

import com.property.meter.dto.AppealDTO;
import com.property.meter.dto.AppealHandleDTO;
import com.property.meter.entity.Appeal;
import com.property.meter.entity.enums.AppealStatus;

import java.util.List;

public interface AppealService {

    Appeal create(AppealDTO dto);

    Appeal handle(AppealHandleDTO dto);

    Appeal getById(Long id);

    Appeal getByAppealNo(String appealNo);

    List<Appeal> listByBill(Long billId);

    List<Appeal> listByRoom(Long roomId);

    List<Appeal> listByStatus(AppealStatus status);

    List<Appeal> listByRoomAndStatus(Long roomId, AppealStatus status);

    List<Appeal> listAll();
}
