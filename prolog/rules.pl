% The assignment is legal if it is explicitly defined
% in the knowledge base, and the assignment is between
% elements that allowed to be assigned to each other.
legalAssignment(X, Y) :-
    assign(X, Y),
    (
        (u(X), ua(Y));
        (ua(X), ua(Y));
        (o(X), oa(Y));
        (oa(X), oa(Y))
    ).

isContained(X, Y) :-
    X = Y;
    legalAssignment(X, Y);
    legalAssignment(X, Z),
    isContained(Z, Y).
% constraints to add:
% - only user attributes can contain users
% - only object attributes can contain objects

decide(U, O, AR) :-
    once((association(UA, OA, ARS),
          member(AR, ARS),
          isContained(U, UA),
          isContained(O, OA),
          \+ disjProhibited(U, O, AR))).

% Our prohibition model right now simulates a default
% exclusion set which includes all policy classes.
% In practice, this means that the only prohibted
% attributes are those whose subtree root is included
% in the inclusion set AT.
disjProhibited(U, AT, AR) :-
    % if there is a disjunctive prohibition such that
    % the access right AR is in the prohibited set of
    % access rights ARS, and the attribute being accessed
    % is in the subtree of an element in the inclusion
    % set.
    disjunctiveProhibition(U_or_UA, ATI, ARS),

    % The access right AR must be in the set of access
    % rights ARS in the defined prohibition.
    member(AR, ARS),

    % Either the user U must unify with U_or_UA or be
    % contained by it.
    isContained(U, U_or_UA),

    % The attribute being accessed AT must be in the
    % inclusion set of the disjunctive range of ATI
    inInclusionSet(AT, ATI).

inInclusionSet(AT, [Head|Tail]) :-
    isContained(AT, Head);
    inInclusionSet(AT, Tail).
