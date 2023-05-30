package se.kth.iv1350.integration;

import se.kth.iv1350.integration.dto.RecordDTO;
import se.kth.iv1350.model.Sale;

public interface AccountingSystem<T> {
    RecordDTO getDataInfo(T dataID) throws AccountRecordNotFoundInAccountingSystemException;
    void updateRegistry(Sale closedSale);
}