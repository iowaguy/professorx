isContained(X, Y) :-
    X = Y;
    assign(X, Y);
    assign(X, Z),
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
