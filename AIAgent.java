import java.util.*;

public class AIAgent{
	Random rand;

	public AIAgent(){
		rand = new Random();
	}

/*
	The method randomMove takes as input a stack of potential moves that the AI agent
	can make. The agent uses a rondom number generator to randomly select a move from
	the inputted Stack and returns this to the calling agent.
*/

	public Move randomMove(Stack possibilities){

		int moveID = rand.nextInt(possibilities.size());
		System.out.println("Agent randomly selected move : "+moveID);
		for(int i=1;i < (possibilities.size()-(moveID));i++){
			possibilities.pop();
		}
		Move selectedMove = (Move)possibilities.pop();
		return selectedMove;
	}

	/*
		Doesn't really care about what happens once the move is made...

		This could mean that the AI agent could take a piece even thought he player
		will gain immediate advantage

		The AI agent takes a pawn with his Queen and in response to this attack,
		the player takes the Queen wth another pawn

		AI after making the move is up one point as the pawn is worth 1
		The Queen has a value of 9 so the player is down 8 points

		Pawn - 1
		Knight/Bishop - 3
		Rook - 5
		Queen - 9
		King - GG

		Get all the possible moves just like above with the random agent 
		and then apply a utility function to work out which move to make

	*/
	public Move nextBestMove(Stack possibilities, Stack blackStack){
		//Move selectedMove = new Move();
		
		//Set a backup of random stack so if possibilities is manipulated we can
		//Still use it to give us a move if there is no base case scenario
		//Where our pieces can move 
		Stack randomStack = (Stack) possibilities.clone();
		//Getting a back up of the black stack so we can manipulate it 
		Stack blackstack = (Stack) blackStack.clone();

		Move bestCase = null;
		Move whiteMove;
		Move move;
		Square blackPos;
		int piecePower = 0;
		int chosenPiecePower = 0;

		// for(int i = 0; i < possibilities.size(); i++){
		// 	System.out.println("possible");
		// 	if(!(possibilities.isEmpty())){
		// 		whiteMove = (Move) possibilities.pop();
		// 		move = whiteMove;

		// 		if((move.getLanding().getYC() > move.getStart().getYC()) &&
		// 			(move.getLanding().getXC() == 3) && (move.getLanding().getYC() == 3) &&
		// 			(move.getLanding().getXC() == 4) && (move.getLanding().getYC() == 4) &&
		// 			(move.getLanding().getXC() == 3) && (move.getLanding().getYC() == 4) &&
		// 			(move.getLanding().getXC() == 4) && (move.getLanding().getYC() == 3)){
		// 				piecePower = 1;

		// 				if(chosenPiecePower < piecePower){
		// 					chosenPiecePower = piecePower;
		// 					bestCase = move;
		// 				}
		// 			}

		// 			for(int j = 0; j < blackstack.size(); j++){
		// 				if(!(possibilities.isEmpty())){
		// 					piecePower = 0;
		// 					blackPos = (Square) blackstack.pop();

		// 					if((move.getLanding().getXC() == blackPos.getXC()) &&
		// 						(move.getLanding().getYC() == blackPos.getYC())){
		// 							if(blackPos.getName().equals("BlackQueen")){
		// 								piecePower = 9;
		// 							}
		// 							else if(blackPos.getName().equals("BlackRook")){
		// 								piecePower = 5;
		// 							}
		// 							else if(blackPos.getName().equals("BlackKnight")){
		// 								piecePower = 3;
		// 							}
		// 							else if(blackPos.getName().equals("BlackBishop")){
		// 								piecePower = 3;
		// 							}
		// 							else if(blackPos.getName().equals("BlackPawn")){
		// 								piecePower = 1;
		// 							}
		// 						}

		// 						if(chosenPiecePower < piecePower){
		// 							chosenPiecePower = piecePower;
		// 							bestCase = move;
		// 						}
		// 				}
		// 			}
		// 			blackstack = (Stack) blackstack.clone();
		// 	}
		// }

		while(!possibilities.empty()){
			whiteMove = (Move) possibilities.pop();
			move = whiteMove;

			//Getting the pieces in the center 
			//Give them power
			if((move.getStart().getYC() < move.getLanding().getYC()) &&
				(move.getLanding().getXC() == 3) && (move.getLanding().getYC() == 3) ||
				(move.getLanding().getXC() == 4) && (move.getLanding().getYC() == 4) ||
				(move.getLanding().getXC() == 4) && (move.getLanding().getYC() == 3) ||
				(move.getLanding().getXC() == 3) && (move.getLanding().getYC() == 4)){
					piecePower = 0;
					//System.out.println("centerpiece");

					//Setting the best move
					if(chosenPiecePower < piecePower){
						
						chosenPiecePower = piecePower;
						bestCase = move;
					}
				}

				while(!blackstack.isEmpty()){
					piecePower = 0;
					blackPos = (Square) blackstack.pop();

					//when the center piece is not being evaluated
					//Getting the value for each piece and determining moves
					//Based on that
					if((move.getLanding().getXC() == blackPos.getXC()) &&
						(move.getLanding().getYC() == blackPos.getYC())){
							if(blackPos.getName().equals("BlackQueen")){
								piecePower = 9;
							}
							if(blackPos.getName().equals("BlackRook")){
								piecePower = 5;
							}
							if(blackPos.getName().equals("BlackKnight")){
								piecePower = 3;
							}
							if(blackPos.getName().equals("BlackBishop")){
								piecePower = 3;
							}
							if(blackPos.getName().equals("BlackPawn")){
								piecePower = 1;
							}
						}

						//Setting the best move
						if(chosenPiecePower < piecePower){
							chosenPiecePower = piecePower;
							bestCase = move;
						}
				}

				blackstack = (Stack) blackstack.clone();
		}

		if(chosenPiecePower > 0){
			System.out.println("-----------------BestCase----------------");
			return bestCase;
		}

		//If we do not have a piece to take, we can make a random move.
		return randomMove(randomStack);
	}

	/*
		This agent extends the funciton of hte agent above... this agent
		looks ahead and tries to determine what the player is going to do 
		next. Just like min-max

		We know how to get the possible movements of all the pieces 
		as we need this functionality for making random moves

		We now able to capture the movements/potential movements of the players 
		pieces exactly as we did for the white pieces

		Once we have this stack of movements we need a utility function to be able
		to calculate the value of the movements and then estimate which movement
		the player will make and then the agent responds

		Random - get all possible moves for white
					select a random move

		NextBest - get all possible moves for white
					create a utility function based on the current move
					this could be if we take a piece we score some points

					loop through the stack of movements and check if we are taking
					a piece and if so make this movement

		TwoLevels - get all possible moves for white
					Then for each of these movements find the best possible resp

					get all possible moves for black after the board changes 
					for each movement of white
					
					rank according to a utility function

					the agent makes the best possible move that it can
					with the least best response from teh player
	*/

	public Move twoLevelsDeep(Stack possibilities, Stack blackStack){
		//Not Completed

		//Extend the function above, implementing two other functions
		//Min and Max
		//Min and Max will be used in this function to navigate the tree and determine
		//the best move
		//One will determine the black players moves and the other white

		//Set a backup of random stack so if possibilities is manipulated we can
		//Still use it to give us a move if there is no base case scenario
		//Where our pieces can move 
		Stack randomStack = (Stack) possibilities.clone();
		//Getting a back up of the black stack so we can manipulate it 
		Stack blackstack = (Stack) blackStack.clone();

		Move bestCase = null;
		Move whiteMove;
		Move move;
		Square blackPos;
		int piecePower = 0;
		int chosenPiecePower = 0;

		while(!possibilities.empty()){
			whiteMove = (Move) possibilities.pop();
			move = whiteMove;

			//Getting the pieces in the center 
			//Give them power
			if((move.getStart().getYC() < move.getLanding().getYC()) &&
				(move.getLanding().getXC() == 3) && (move.getLanding().getYC() == 3) ||
				(move.getLanding().getXC() == 4) && (move.getLanding().getYC() == 4) ||
				(move.getLanding().getXC() == 4) && (move.getLanding().getYC() == 3) ||
				(move.getLanding().getXC() == 3) && (move.getLanding().getYC() == 4)){
					piecePower = 0;
					//System.out.println("centerpiece");

					//Setting the best move
					if(chosenPiecePower < piecePower){
						
						chosenPiecePower = piecePower;
						bestCase = move;
					}
				}

				while(!blackstack.isEmpty()){
					piecePower = 0;
					blackPos = (Square) blackstack.pop();

					//when the center piece is not being evaluated
					//Getting the value for each piece and determining moves
					//Based on that
					if((move.getLanding().getXC() == blackPos.getXC()) &&
						(move.getLanding().getYC() == blackPos.getYC())){
							if(blackPos.getName().equals("BlackQueen")){
								piecePower = 9;
							}
							if(blackPos.getName().equals("BlackRook")){
								piecePower = 5;
							}
							if(blackPos.getName().equals("BlackKnight")){
								piecePower = 3;
							}
							if(blackPos.getName().equals("BlackBishop")){
								piecePower = 3;
							}
							if(blackPos.getName().equals("BlackPawn")){
								piecePower = 1;
							}
						}

						//Setting the best move
						if(chosenPiecePower < piecePower){
							chosenPiecePower = piecePower;
							bestCase = move;
						}
				}

				blackstack = (Stack) blackstack.clone();
		}

		if(chosenPiecePower > 0){
			System.out.println("-----------------BestCase----------------");
			return bestCase;
		}

		return bestCase;
	}
}
