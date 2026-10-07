package com.justice.transfer_service.model;

public enum TransferStatus {
    CREATED,
    ANALYZING,
    READY,
    EXECUTING,
    COMPLETED,
    PARTIALLY_COMPLETED,
    FAILED
}
