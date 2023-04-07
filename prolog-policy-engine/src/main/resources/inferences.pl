inference1(U, O, AR, NewAssoc, PreviousDecision) :-
    PreviousDecision = grant;
    (
        % For all possible UA' where we have associations
        % for OA with access rights AR.
        NewAssoc = association(UA_prime, OA, ARS),

        % An association for UA' does not exist in the
        % pre-mutation policy for object O or object
        % attribute OA.
        \+ (association(UA_prime, OA, AR);
            association(UA_prime, O, AR)),

        % Ensure that the new association is relevant for
        % the object being accessed O.
        isContained(O, OA),

        % U is contained in the new user attribute UA_prime
        isContained(U, UA_prime),

        % The permission being used is relevant to the new
        % association.
        member(AR,ARS)
    ).
