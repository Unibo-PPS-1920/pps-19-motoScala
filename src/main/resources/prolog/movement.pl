%move(+start,+end,-[Directions]).

move(T,T,[T]):- !.

move((SX,SY),(TX,TY),[(DX,DY)|N]):-
  step(SX,TX,NX),
  step(SY,TY,NY),
  pos_dir((SX,SY),(NX,NY),(DX,DY)),
  move((NX,NY),(TX,TY),N).

move2(T,T,T):- !.

move2((SX,SY),(TX,TY),(DX,DY)):-
  step(SX,TX,NX),
  step(SY,TY,NY),
  pos_dir((SX,SY),(NX,NY),(DX,DY)).
  %move2((NX,NY),(TX,TY),N).


step(P,P,P).

step(P,T,A):-
  P > T,
  A is P - 1.

step(P,T,A):-
  P < T,
  A is P + 1.

pos_dir(P1,P1,(0,0)).

pos_dir((X1,Y1),(X2,Y2),(DX,DY)):-
	DX is X2 - X1,
	DY is Y2 - Y1.

