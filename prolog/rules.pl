isContained(X, Y) :-
    X = Y;
    assign(X, Y);
    assign(X, Z),
    isContained(Z, Y).

decide(U, O, AR) :-
    once((association(UA, OA, ARS),
          member(AR, ARS),
          isContained(U, UA),
          isContained(O, OA))).

