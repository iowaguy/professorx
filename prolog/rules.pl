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

% Associations must be defined in the knowledge base and
% be between a UA and an OA, or between a UA and an O. In
% either case, they must have legal access rights.
legalAssociation(UA, OA, ARS) :-
    association(UA, OA, ARS),
    (
        (ua(UA), oa(OA), legalAccessRights(ARS));
        (ua(UA), o(OA), legalAccessRights(ARS))
    ).

% The list of access rights must have at least one element
% and the elements in the list must each be access rights ar.
legalAccessRights([H|T]) :-
    (ar(H), T = []);
    (ar(H), legalAccessRights(T)).

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
    legalAccessRights(ARS),
    (o(AT); oa(AT)),

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

decide(U, O, AR) :-
    once((legalAssociation(UA, OA, ARS),
          member(AR, ARS),
          isContained(U, UA),
          isContained(O, OA),
          \+ disjProhibited(U, O, AR))).
