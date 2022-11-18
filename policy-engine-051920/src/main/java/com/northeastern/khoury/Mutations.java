package com.northeastern.khoury;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;

public enum Mutations {
    // ATTRIBUTE_EXCHANGE_SOURCE,
    ATTRIBUTE_EXCHANGE_SOURCE_EXPLICIT;

    // public Policy apply(Policy policy) {
    //     switch (this) {
    //         case ATTRIBUTE_EXCHANGE_SOURCE:
    //             return attributeExchangeSource(policy);
    //         default:
    //             return null;
    //     }
    // }

    public PolicyImpl applyExplicit(PolicyImpl policy, String source, String dest, String newSource,
                                    String newDest, OperationSet newOps) throws PMException {
        switch (this) {
            case ATTRIBUTE_EXCHANGE_SOURCE_EXPLICIT:
                return attributeExchangeSourceExplicit(policy, source, dest, newSource);
            default:
                return null;
        }
    }

    private PolicyImpl attributeExchangeSourceExplicit(PolicyImpl policy, String source, String dest,
                                                       String newSource) throws PMException {
        return policy.attributeExchangeSource(source, dest, newSource);
    }

    // private Policy attributeExchange(Policy policy) throws PMException {
    //     return policy.attributeExchangeSource(source, dest, newSource);
    // }
}
