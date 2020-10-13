%move(+start,+end,-[Directions]).

move(T,T,[T]):- !.

move((SX,SY),(TX,TY),[(DX,DY)|N]):-
  step(SX,TX,NX),
  step(SY,TY,NY),
  pos_dir((SX,SY),(NX,NY),(DX,DY)),
  move((NX,NY),(TX,TY),N).

move2((TX,TY),(TX,TY),TX,TY):- !.

move2((SX,SY),(TX,TY),DX,DY):-
  step(SX,TX,NX),
  step(SY,TY,NY),
  pos_dir((SX,SY),(NX,NY),(DX,DY)).
  %move2((NX,NY),(TX,TY),N).

move_avoiding((TX,TY),(TX,TY),[],TX,TY):- !.

move_avoiding((SX,SY),(TX,TY),O,DX,DY):-
  step(SX,TX,NX),
  step(SY,TY,NY),
  allowed_move((NX,NY),O),
  pos_dir((SX,SY),(NX,NY),(DX,DY)).

move_avoiding((SX,SY),(TX,TY),O,DX,DY):-
  step(SX,TX,NX),
  step(SY,TY,NY),
  \+ allowed_move((NX,NY),O),
  step_right(SX,EX),
  step_right(SY,EY),
  pos_dir((EX,EY),(NX,NY),(DX,DY)).

allowed_move((X,Y),O):-
  \+ member((X,Y),O).

step_right(X0,X1):-
	X1 is X0 + 1.


step(P,P,P).

step(P,T,E):-
  P > T,
  E is P - 1.

step(P,T,E):-
  P < T,
  E is P + 1.

pos_dir(P1,P1,(0,0)).

pos_dir((X1,Y1),(X2,Y2),(DX,DY)):-
	DX is X2 - X1,
	DY is Y2 - Y1.

