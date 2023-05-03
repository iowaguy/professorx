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
oa(oa1).
oa(oa2).
oa(oa3).
oa(oa4).
oa(oa5).
oa(oa6).
oa(oa7).
pc(department).

ar(permission0).
ar(permission1).
ar(permission2).
ar(permission4).

% User and user attribute assignments.
assign(ua1,department).
assign(ua2,department).
assign(ua3,ua2).
assign(ua4,department).
assign(ua5,department).
assign(ua6,ua5).
assign(ua7,ua6).
assign(ua6,ua4).
assign(ua3,ua1).
assign(u1,ua7).
assign(u2,ua1).

% Object and object attribute assignments.
assign(oa1,department).
assign(oa2,department).
assign(oa3,oa2).
assign(oa4,department).
assign(oa5,department).
assign(oa6,oa5).
assign(oa7,oa6).
assign(oa6,oa4).
assign(oa7,oa3).
assign(oa3,oa1).
assign(o1,oa7).

% Associations
association(ua5,oa5,[permission0]).
association(ua4,oa5,[permission0]).
association(ua4,oa4,[permission4]).
association(ua4,oa2,[permission4]).
association(ua2,oa2,[permission0]).
association(ua1,oa4,[permission1, permission2]).

%% This should not be allowed
association(u1,o1,[permission1]).

% Decisions, pre-mutation
%% decision(u2, o1, p0, deny).
%% decision(u1, o1, p0, grant).
%% decision(u2, o1, p4, deny).
%% decision(u1, o1, p4, grant).

% Effect of the following: u2 cannot do permission1 on o1
disjunctiveProhibition(u2, [oa7], [permission1]).

%% Invalid prohibition. Object cannot be the "object"
disjunctiveProhibition(u2, [o1], [permission2]).
