pc(department).
u(u1).
u(u2).
ua(ua2).
ua(ua1).
o(o1).
oa(oa1).
ar(p1).
ar(p2).
assign(u2,ua2).
assign(o1,oa1).
assign(oa1,department).
assign(ua1,department).
assign(ua2,department).
assign(u1,ua1).
association(ua2,oa1,[p2]).
association(ua1,oa1,[p1]).
disjunctiveProhibition(u2, [o1], [p1]).
