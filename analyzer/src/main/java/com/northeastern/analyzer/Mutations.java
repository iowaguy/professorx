package com.northeastern.analyzer;

import java.util.Set;

import com.northeastern.policy.MyPMException;

import com.northeastern.policy.Policy;

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

    public Policy applyExplicit(Policy policy, String source, String dest, String newSource,
                                    String newDest, Set<String> newOps) throws MyPMException {
        switch (this) {
            case ATTRIBUTE_EXCHANGE_SOURCE_EXPLICIT:
                return attributeExchangeSourceExplicit(policy, source, dest, newSource);
            default:
                return null;
        }
    }

    private Policy attributeExchangeSourceExplicit(Policy policy, String source, String dest,
                                                       String newSource) throws MyPMException {
        return policy.attributeExchangeSource(source, dest, newSource);
    }

    // private Policy attributeExchange(Policy policy) throws PMException {
    //     return policy.attributeExchangeSource(source, dest, newSource);
    // }
}
