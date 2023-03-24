%%%%% These Facts define a test policy. %%%%%%%
u(u1).
u(u2).
ua(ua1).
ua(ua2).
ua(ua3).
ua(ua4).
ua(ua5).
ua(ua6).
ua(ua7).
o(o1).
o(o2).
oa(oa1).
oa(oa2).
oa(oa3).
oa(oa4).
oa(oa5).
oa(oa6).
oa(oa7).
pc(pc1).

ar(p0).
ar(p1).
ar(p2).
ar(p4).

% User and user attribute assignments.
assign(u1,ua7).
assign(u2,ua1).
assign(ua7,ua3).
assign(ua7,ua6).
assign(ua3,ua1).
assign(ua3,ua2).
assign(ua1,pc1).
assign(ua2,pc1).
assign(ua4,pc1).
assign(ua5,pc1).
assign(ua6,ua5).
assign(ua6,ua4).

% Object and object attribute assignments.
assign(o1,oa7).
assign(o2,oa5).
assign(oa7,oa3).
assign(oa7,oa6).
assign(oa3,oa1).
assign(oa3,oa2).
assign(oa6,oa4).
assign(oa6,oa5).

% illegal assignment, should be ignored
assign(oa3,o1).

% Associations
association(ua5,oa5,[p0]).
association(ua4,oa5,[p0]).
association(ua4,oa4,[p4]).
association(ua4,oa2,[p4]).
association(ua2,oa2,[p0]).
association(ua1,oa7,[p1]).

% an association that only applies to the illegal assignment
% above.
association(ua7,o1,[p2]).

% Decisions, pre-mutation
decision(u2, o1, p0, deny).
decision(u1, o1, p0, grant).
decision(u2, o1, p4, deny).
decision(u1, o1, p4, grant).

% Effect of the following: u2 cannot do p1 on o1
disjunctiveProhibition(u2, [oa7], [p1]).
