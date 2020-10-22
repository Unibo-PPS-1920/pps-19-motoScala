%outputs direction to move in order to reach target, avoiding obstacles and with a bias ont the target's position
move_avoiding(_,(TX,TY),(TX,TY),[],TX,TY):-
    !.

move_avoiding(Q,(SX,SY),(TX,TY),O,OX,OY):-
    BX is TX + Q,
    BY is TY + Q,
    step(SX,BX,NX),
    step(SY,BY,NY),
    allowed_move((NX,NY),O),
    pos_dir((SX,SY),(NX,NY),(OX,OY)).

move_avoiding(Q,(SX,SY),(TX,TY),O,DX,DY):-
    BX is TX + Q,
    BY is TY + Q,
    step(SX,BX,NX),
    step(SY,BY,NY),
    \+ allowed_move((NX,NY),O),
    random_biased_dir(100,DX,DY).

allowed_move((X,Y),O):-
  \+ member((X,Y),O).

step(P,P,P).

step(P,T,E):-
  P > T,
  E is P - 1.

step(P,T,E):-
  P < T,
  E is P + 1.

%given two positions outputs the diection in order to go from one to the other

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




