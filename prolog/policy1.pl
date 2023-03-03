%%%%% These Facts define a test policy. %%%%%%%

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
assign(oa7,oa3).
assign(oa7,oa6).
assign(oa3,oa1).
assign(oa3,oa2).
assign(oa6,oa4).
assign(oa6,oa5).

% Associations
association(ua5,oa5,[p0]).
association(ua4,oa5,[p0]).
association(ua4,oa4,[p4]).
association(ua4,oa2,[p4]).
association(ua2,oa2,[p0]).
%% association(ua1,o1,p0).

% Decisions, pre-mutation
decision(u2, o1, p0, deny).
decision(u1, o1, p0, grant).
decision(u2, o1, p4, deny).
decision(u1, o1, p4, grant).
