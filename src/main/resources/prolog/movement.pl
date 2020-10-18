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

move_avoiding(Q,(TX,TY),(TX,TY),[],TX,TY):-
	!.

move_avoiding(Q,(SX,SY),(TX,TY),O,OX,OY):-
  step(SX,TX,NX),
  step(SY,TY,NY),
  allowed_move((NX,NY),O),
  pos_dir((SX,SY),(NX,NY),(DX,DY)),
  random_biased_dir(Q,(DX,DY),(OX,OY)).

move_avoiding(Q,(SX,SY),(TX,TY),O,DX,DY):-
  step(SX,TX,NX),
  step(SY,TY,NY),
  \+ allowed_move((NX,NY),O),
  random_biased_dir(100,DX,DY).

move_avoiding2(Q,(TX,TY),(TX,TY),[],TX,TY):-
    !.

move_avoiding2(Q,(SX,SY),(TX,TY),O,OX,OY):-
    BX is TX + Q,
    BY is TY + Q,
    step(SX,BX,NX),
    step(SY,BY,NY),
    allowed_move((NX,NY),O),
    pos_dir((SX,SY),(NX,NY),(OX,OY)).
    %random_biased_dir(Q,(DX,DY),(OX,OY)).

move_avoiding2(Q,(SX,SY),(TX,TY),O,DX,DY):-
    BX is TX + Q,
    BY is TY + Q,
    step(SX,BX,NX),
    step(SY,BY,NY),
    \+ allowed_move((NX,NY),O),
    random_biased_dir(100,DX,DY).

allowed_move((X,Y),O):-
  \+ member((X,Y),O).

step_right(X0,X1):-
	X1 is X0 + 10.


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

random_dir(X,Y):-
	rand_int(3,A),
	rand_int(3,B),
	X is A -1,
	Y is B -1.

%random_biased_dir(+B,+X,+Y,-RX,-RY).
%outputs a random direction with more probabilty as the bias rises

random_biased_dir(B,(X,Y),(RX,RY)):-
	rand_int(B,R),
	(R > 1 ->
	random_dir(RX,RY);
	RX is X,
	RY is Y).




