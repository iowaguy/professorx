u(u1).
u(u2).
ua(ua1).
ua(ua2).
o(o1).
oa(oa1).
pc(department).
ar(p1).
ar(p2).
assign(ua1,department).
assign(ua2,department).
assign(u1,ua1).
assign(u2,ua2).
assign(oa1,department).
assign(o1,oa1).
association(ua1,oa1,[p1]).
association(ua2,oa1,[p2]).
disjunctiveProhibition(u2, [o1], [p1]).
