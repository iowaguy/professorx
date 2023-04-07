package com.northeastern.prologpolicyengine;

import com.northeastern.policy.MyPMException;
import com.northeastern.policy.Policy;
import com.northeastern.policy.ResourceAccess;
import org.jpl7.Atom;
import org.jpl7.Query;
import org.jpl7.Term;

public class PrologPolicyEngine {
    private Query rules;
    private Query policy;

    public PrologPolicyEngine(String rulesPath) throws MyPMException {
        this.rules = new Query( "consult", new Term[] {new Atom(rulesPath)});
        if (!this.rules.hasSolution()) {
            throw new MyPMException("Could not load Prolog rules.");
        }
    }

    public void loadPolicy(Policy policy) {
        // TODO do this eventually
    }

    public void loadPolicy(String policyPath) throws MyPMException {
        if (this.policy != null && this.policy.isOpen()) {
            this.policy.close();
        }

        this.policy = new Query( "consult", new Term[] {new Atom(policyPath)});
        if (!this.policy.hasSolution()) {
            throw new MyPMException("Could not load Prolog policy.");
        }
    }

    public boolean getDecision(ResourceAccess access) throws MyPMException {
        return getDecision(access.getSubject(), access.getObject(), access.getPermissions());
    }

    public boolean getDecision(String subject, String object, String action) throws MyPMException {
        Query query = new Query( "decide",
                new Term[] {
                        new Atom(subject),
                        new Atom(object),
                        new Atom(action)
        });
        return query.hasSolution();
    }
}