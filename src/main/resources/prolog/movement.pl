move(T,T,[T]):- !.

move((SX,SY),(TX,TY),[(NX,NY)|N]):-
  step(SX,TX,NX),
  step(SY,TY,NY),
  move((NX,NY),(TX,TY),N).

step(P,P,P).

step(P,T,A):-
  P > T,
  A is P - 1.

step(P,T,A):-
  P < T,
  A is P + 1.
