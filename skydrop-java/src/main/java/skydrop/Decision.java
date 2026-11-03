package skydrop;

/** The result of trying to place an order. */
public enum Decision {
    ACCEPTED,
    REJECTED_NO_FLY_ZONE,   // FR-3
    REJECTED_OUT_OF_RANGE,  // FR-4
    REJECTED_OFF_MAP        // FR-7 (Lab 3): defined, but addOrder() never returns it yet
}
