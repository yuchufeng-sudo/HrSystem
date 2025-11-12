package com.ys.hr.sign.strategy;

import com.ys.hr.sign.entiry.ResponseEntity;

/**
 * Electronic Signature Platform Strategy Interface
 */
public interface SignApiStrategy<T, R> {

    /**
     * Send signature Name
     * @param params
     * @return
     */
    ResponseEntity<R> send(T params);

    /**
     * Query Signing Information
     * @param params
     * @return
     */
    ResponseEntity<R> getSignDetail(T params);

}
