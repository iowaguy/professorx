u(u1).
u(u2).
u(u3).
u(u4).
ua(ua1).
ua(ua2).
o(o1).
o(o6).
oa(oa1).
oa(oa5).
pc(department).
ar(p1).
ar(p2).
assign(ua1,department).
assign(ua2,department).
assign(u1,ua1).
assign(u2,ua2).
assign(oa1,department).
assign(o1,oa1).
assign(u3,ua1).
assign(u4,ua1).
assign(oa5,department).
assign(o6,oa5).
assign(u1,ua2).
association(ua1,oa1,[p1]).
association(ua2,oa1,[p2]).
disjunctiveProhibition(u2, [o1], [p1]).
disjunctiveProhibition(u2, [ua1], [p1]).
