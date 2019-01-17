class Move{
  Square start;
  Square landing;

  //If you extend the class Move to include an evaluation of the move
  //The output of the utility function you won't need to create an additional data structure for keeping
  //track of the scores of each of the movements

  public Move(Square x, Square y){
    start = x;
    landing = y;
  }

  public Move(){
    
  }

  public Square getStart(){
    return start;
  }

  public Square getLanding(){
    return landing;
  }
}
