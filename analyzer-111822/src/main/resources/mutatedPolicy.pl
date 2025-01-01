u(u1).
u(u2).
ua(ua1).
ua(ua2).
ua(ua5).
ua(ua6).
o(o1).
o(o8).
oa(oa1).
oa(oa4).
oa(oa9).
pc(department).
pc(pc3).
pc(pc7).
ar(p1).
ar(p2).
assign(ua1,department).
assign(ua2,department).
assign(u1,ua1).
assign(u2,ua2).
assign(oa1,department).
assign(o1,oa1).
assign(oa4,department).
assign(ua5,ua2).
assign(ua6,pc3).
assign(o8,oa1).
assign(oa9,pc7).
association(ua1,oa1,[p1]).
association(ua2,oa1,[p2]).
disjunctiveProhibition(u2, [o1], [p1]).
disjunctiveProhibition(u2, [o8], [p2]).
disjunctiveProhibition(u1, [oa4], [p1]).
