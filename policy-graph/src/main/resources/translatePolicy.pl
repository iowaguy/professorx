pc(class).
u(u1).
u(u2).
ua(ua2).
ua(ua1).
o(o1).
oa(oa1).
ar(p1).
ar(p2).
assign(ua1,class).
assign(oa1,class).
assign(u1,ua1).
assign(ua2,class).
assign(u2,ua2).
assign(o1,oa1).
association(ua2,oa1,[p2]).
association(ua1,oa1,[p1]).
disjunctiveProhibition(u2, [o1], [p1]).
