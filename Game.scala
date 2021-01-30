package cw
//P2523292
/**
 * This class holds an instance of a simple game where 
 * a player moves on a field and collects bounties.
 * See the explanation sheet and comments in this file for details. The constructor builds an
 * instance of a game
 * 
 * @param wall A list of coordinates (as tuples) where walls exist. Example: The parameter List((0,0),(0,1)) puts two wall elements in the upper left corner and the position below.
 * @param bounty A list of bounties, each is a position and a function (i.e. a 3 value tuple). Example: List((0,0,(x: Int)=>x+1)) puts a bounty in the upper left corner which adds 1 to the score.
 * @param playerX The initial x position of the player.
 * @param playerY The initial y position of the player. If playerX and playerY are 0, the player starts in the upper left corner. As the player moves these positions update.
 */
class Game(wall: List[(Int, Int)], bounty: List[(Int,Int, Int=> Int)], var playerX: Int, var playerY: Int) {

	//the current grid, a 10x10 field, initially all cells are false (i.e. no walls)
	private var field: Array[Array[Boolean]] = Array.ofDim[Boolean](10, 10)

			//a separate grid holding bounties at relevant positions, initially all cells are set to null (i.e. no bounties)
			private var bounties: Array[Array[Int=>Int]] = Array.ofDim[Int=>Int](10, 10)

			/* Please note - to align with the overall case study (see explanation sheet), both of the above two-dimensional arrays 
			 * should be accessed in the format field(col)(row) so field(2)(0) would retrieve the 3rd column and the 1st row (as indexing starts at zero), 
			 * equivalent to an (x,y) coordinate of (2,0). You may therefore visualise each inner array as representing a column of data.
			 */

			//the current score, initially 0
			private var score: Int = 0

			//the current X and Y save position, initially -1
			private var saveX: Int = -1
			private var saveY: Int = -1

			/* This code is executed as part of the constructor. 
			 * It uses the list of walls provided to initialise the walls in the field array by setting given coordinates to true.
			 * It then uses the list of bounties to initialise the corresponding array by setting given coordinates to the provided function.
			 */
			wall.foreach(w => field(w._1)(w._2)=true)
			bounty.foreach(w => bounties(w._1)(w._2)=w._3)


			/**
			 * Repeatedly run a sequence of commands. For example:
			 *    for(i <- 1 to 5) println("Hello")
			 * can be replaced by
			 *    rpt(5)(println("Hello"))
			 */
			def rpt (n: Int) ( commands: => Unit ) {
				for (i <- 1 to n) { commands }
			}

			/********************************************************************************
			 * COURSEWORK STARTS HERE - COMPLETE THE DEFINITIONS OF EACH OF THE OPERATIONS
			 * WE SUGGEST YOU RUN THE GameTest SUITE AFTER EVERY CHANGE YOU MAKE TO THESE
			 * SO YOU CAN SEE PROGRESS AND CHECK THAT YOU'VE NOT BROKEN ANYTHING THAT USED
			 * TO WORK.
			 *******************************************************************************/


			/**
			 * Returns the current position of the player as a tuple, in (x,y) order.
			 */
			def getPlayerPos(): (Int, Int) = {
					return(playerX,playerY)
			}


			/**
			 * Updates saveX and saveY to the current player position.
			 */
			def save(): Unit = {
					saveX = playerX
							saveY = playerY
			}

			/**
			 * Returns the current score.
			 */
			def getScore(): Int = return score


					/**
					 * Returns the current save position as a tuple, in (x,y) order.
					 */
					def getSavePos(): (Int, Int) =  {
							return (saveX,saveY);
					}

					/**
					 * AL: Arrow Left.  Move the player one place to the left. If
					 * there is a wall or the field ends, nothing happens. If there
					 * is a bounty, it is collected. If 9 or more fields are covered 
					 * from a saved position, the bounties in the rectangle are collected.
					 */
					def al() {
						if (playerX > 0 && field(playerX-1)(playerY) == false) {
							playerX -= 1
									checkBounty()
									checkBounties()
						}
					}

					/**
					 * AR: Arrow Right.  Move the player one place to the right. If
					 * there is a wall or the field ends, nothing happens. If there
					 * is a bounty, it is collected. If 9 or more fields are covered 
					 * from a saved position, the bounties in the rectangle are collected.
					 */
					def ar() {
						if (playerX < 9 && field(playerX + 1)(playerY) == false){
							playerX += 1
									checkBounty()
									checkBounties()
						}
					}

					/**
					 * AU: Arrow Up.  Move the player one place up. If
					 * there is a wall or the field ends, nothing happens. If there
					 * is a bounty, it is collected. If 9 or more fields are covered 
					 * from a saved position, the bounties in the rectangle are collected.
					 */
					def au() {
						if (playerY > 0 && field(playerX)(playerY - 1) == false){
							playerY -= 1
									checkBounty()
									checkBounties()
						}
					}

					/**
					 * AD: Arrow Down.  Move the player one place down. If
					 * there is a wall or the field ends, nothing happens. If there
					 * is a bounty, it is collected. If 9 or more fields are covered 
					 * from a saved position, the bounties in the rectangle are collected.
					 */
					def ad() {
						if (playerY < 9 && field(playerX)(playerY + 1) == false){
							playerY += 1
									checkBounty()
									checkBounties()

						}
					}

					/**
					 * AL: Arrow Left n.  Move the player n places to the left. If
					 * there is a wall or the field ends, the player stops before 
					 * the wall or end of field. Any bounties are collected and if 
					 * 9 or more fields are covered from a saved position after an 
					 * individual move, the bounties in the rectangle are collected.
					 * Negative numbers or 0 as a parameter cause no effect.
					 */
					def al(n: Int) {

						if (n > 0){
							rpt(n) {
								al()
							}
						}
					}

					/**
					 * AR: Arrow Right n.  Move the player n places to the right. If
					 * there is a wall or the field ends, the player stops before 
					 * the wall or end of field. Any bounties are collected and if 
					 * 9 or more fields are covered from a saved position after an 
					 * individual move, the bounties in the rectangle are collected.
					 * Negative numbers or 0 as a parameter cause no effect.
					 */
					def ar(n: Int) {
						if (n > 0){
							rpt(n) {
								ar()
							}
						}
					}

					/**
					 * AU: Arrow Up n.  Move the player n places to up. If
					 * there is a wall or the field ends, the player stops before 
					 * the wall or end of field. Any bounties are collected and if 
					 * 9 or more fields are covered from a saved position after an 
					 * individual move, the bounties in the rectangle are collected.
					 * Negative numbers or 0 as a parameter cause no effect.
					 */
					def au(n: Int) {
						if (n > 0){
							rpt(n) {
								au()

							}
						}
					}

					/**
					 * AD: Arrow Down n.  Move the player n places down. If
					 * there is a wall or the field ends, the player stops before 
					 * the wall or end of field. Any bounties are collected and if 
					 * 9 or more fields are covered from a saved position after an 
					 * individual move, the bounties in the rectangle are collected.
					 * Negative numbers or 0 as a parameter cause no effect.
					 */
					def ad(n: Int) {
						if (n > 0){
							rpt(n) {
								ad()

							}
						}
					}

					/**
					 * Checks if the current position is a bounty. A bounty exists if the cell is not
					 * set to null. If a bounty does exist, increase the score, 
					 * and then erase the bounty, i.e. set it back to null.
					 */
					def checkBounty() {

						if (bounties(playerX)(playerY) != null){

							score = bounties(playerX)(playerY)(score)

									bounties(playerX)(playerY) = null 

						}
					}

					//The methods beyond this point (aside to those in GameProducer which is a separate task) are more complex than those above.

					/**
					 * This moves the player according to a string. The string can contain the 
					 * letters l, r, u, d representing left, right, up, down moves.  If
					 * there is a wall or the field ends, the individual move is not 
					 * executed. Any further moves are done. Any bounties are collected and the
					 * save position is evaluated.
					 */
					def move(s: String) {
						var current = 'm'

								for (i <- 0 to s.length-1){
									current = s(i).toLower
											current match{
											case 'l' => al()
											case 'r' => ar()
											case 'u' => au()
											case 'd' => ad()
											case _ => null
									}
								}
					}

					/**
					 * Identifies the maximum overall bounty in the game. This is the sum 
					 * of the current score and the possible score from applying all of the remaining bounties.
					 * No bounties are collected here, only the max score is returned.
					 */
					def maxBounty(): Int = {

							var maxScore = 0
									var currentScore = score 
									var bounty = 0
									for (i <- 0 to 9){
										for (j <- 0 to 9){
											if (bounties(i)(j) != null){
												bounty +=  bounties(i)(j)(score) - currentScore
											}
										}
									}
							maxScore = bounty + currentScore
									return maxScore
					}

					/**
					 * Checks if the rectangle defined by the current position and saved position 
					 * covers nine or more positions. If yes, it collects bounties in it, increases the 
					 * score, and erases the bounties.
					 */
					def checkBounties() {
						var areaCovered = 0
								var currentScore = score
								var bounty = 0
								var greatestX = 0
								var greatestY = 0
								var lowestX = 0
								var lowestY = 0


								if (saveX != -1 && saveY != -1){
									areaCovered = (playerX-saveX + 1 )*(playerY-saveY + 1 )
											if (areaCovered < 0)
												areaCovered = areaCovered * -1
								}
						
						if (saveX > playerX) {
						  greatestX = saveX
						  lowestX = playerX
						} else {
						  greatestX = playerX
						  lowestX = saveX
						} 
							if (saveY > playerY) {
						  greatestY = saveY
						  lowestY = playerY
						} else {
						  greatestY = playerY
						  lowestY = saveY
						} 


						if (areaCovered >= 9){
							for ( i <- lowestX to greatestX){
								for ( j <- lowestY to greatestY){
									if (bounties(i)(j) != null){
										bounty += bounties(i)(j)(score) - currentScore

												bounties(i)(j) = null 

									}

								}
							}
							score = currentScore + bounty
									saveX = -1
									saveY = -1
						}

					}


					/**
					 * This gives a string in the format for move, which collects the maximum bounty. No specific
					 * requirements for the efficiency of the solution exist, but the solution must consist of a finite number 
					 * of steps. The move is combined of a number of moves
					 * given by suggestMove. If these are not possible, an empty string is returned. No bounties are collected 
					 * and the player must be at the original position after the execution of the method.
					 */
					def suggestSolution(): String = {
							var tempX = playerX
									var tempY = playerY
									var tempI = new Array[Int](2)
									var tempJ = new Array[Int](2)
									var counter = 0
									var answer = ""

									for (i <- 0 to 9) {
										for (j <- 0 to 9){
											if (bounties(i)(j) != null){
												tempI(counter) = i
														tempJ(counter) = j
														counter += 1

											} 
										}
									}
							answer = suggestMove(tempI(0),tempJ(0))

									playerX = tempI(0)
									playerY = tempJ(0)

									if(answer != ""){
										answer = answer + suggestMove(tempI(1),tempJ(1))

										playerX = tempX
										playerY = tempY


									} 
							return answer
					}

					/**
					 * This gives a string in the format for move, which moves from the current position to 
					 * position x,y. No specific requirements for the efficiency of the solution exist. The move
					 * cannot jump walls. The method is restricted to finding a path which is combined of a number of 
					 * left and then a number of up movement, or left/down, or right/up, or right/down movements only.
					 * If this is not possible due to walls, it returns an empty string. No actual move is done. If 
					 * x or y are outside the field, an empty string is returned as well.
					 */
					def suggestMove(x: Int, y: Int): String = {
							var move1:String = ""
									var move2:String = ""
									var tempX = playerX
									var tempY = playerY
									var xDirection = 0
									var yDirection = 0
									var length = 0

									xDirection = (x - tempX)
									yDirection = (y - tempY)

									if (xDirection < 0){
										xDirection = xDirection * -1
									}
					if (yDirection < 0){
						yDirection = yDirection * -1
					}

					length = xDirection + yDirection


							if((x < 0 || x > 9) || (y < 0 || y > 9)){
								move1 = ""
										move2 = ""
							}else	if (field(x)(y) == true){
								move1 = ""
										move2= ""
							} else {

								for (i <- 0 to xDirection){
									if (x > tempX){
										if (field(tempX + 1)(tempY) == false && tempX < 9) {
											tempX += 1
													move1 += 'r'
										}
									} else if (x < tempX){
										if (field(tempX - 1)(tempY) == false && tempX > 0){
											tempX -= 1
													move1 += 'l'
										}
									}
								}

								for (i <- 0 to yDirection){
									if (y > tempY){
										if (field(tempX )(tempY + 1) == false && tempY < 9) {
											tempY += 1
													move1 += 'd'
										}
									} else if (y < tempY){
										if (field(tempX )(tempY - 1) == false && tempY > 0){
											tempY -= 1
													move1 += 'u'
										}
									}
								}

								tempX = playerX
										tempY = playerY

										for (i <- 0 to yDirection){
											if (y > tempY){
												if (field(tempX )(tempY + 1) == false && tempY < 9) {
													tempY += 1
															move2 += 'd'
												}
											} else if (y < tempY){
												if (field(tempX )(tempY - 1) == false && tempY > 0){
													tempY -= 1
															move2 += 'u'
												}
											}
										}
								for (i <- 0 to xDirection){
									if (x > tempX){
										if (field(tempX + 1)(tempY) == false && tempX < 9) {
											tempX += 1
													move2 += 'r'
										}
									} else if (x < tempX){
										if (field(tempX - 1)(tempY) == false && tempX > 0){
											tempX -= 1
													move2 += 'l'
										}
									}
								}
							}
					if (move1 == "" && move2 == ""){
						return move1
					}else if (move1.length() == length && move2.length() != length){
						return move1
					} else if (move2.length() == length && move1.length() != length){
						return move2
					} else if (move1.length() == length && move2.length() == length){
						return move1	
					} else 
						""
					}

					/* This method is already implemented. You should not change it */
					/**
					 * Sets the savePos to the values of the parameters. This method is
					 * for testing only. Normally, save() is used for this purpose.
					 */
					def setSavePos(saveX: Int, saveY: Int): Unit =  {
							this.saveX=saveX
									this.saveY=saveY
					}

}

/**
 * This object returns a standard instance of Game
 *
 */
object GameProducer{


	/**
	 * @return A game with 
	 * - walls in positions 3,0 3,1 and 3,2
	 * - a bounty at 4,1 which increases score by 5
	 * - a bounty at 3,3 which increases score by 10
	 * - the player in position 0,0
	 */
	def initialiseTest1(): Game = {
			return new Game(List((3,0),(3,1),(3,2)), List((4,1, score =>score + 5),(3,3, score => score + 10)), 0, 0)
	}

	/**
	 * @return A game with 
	 * - walls in positions 3,3 3,4 3,5 5,3 5,4 and 5,5
	 * - a bounty at 4,4 which increases score by 1
	 * - a bounty at 6,3 which increases score by 1 if the current score is 0 or else increases the score by 3
	 * - the player in position 3,2
	 */
	def initialiseTest2(): Game = {
			return new Game(List((3,3),(3,4),(3,5),(5,3),(5,4),(5,5)), List((4,4, score => score +1) ,(6,3,score => if (score == 0) score + 1 else score + 3)), 3, 2)  
	}

	/**
	 * @return A game with 
	 * - walls in positions 3,0 3,1 and 3,2
	 * - a bounty at 4,1 which increases score by 5
	 * - a bounty at 3,3 which increases score by 10
	 * - the player in position 4,1
	 */
	def initialiseTest3(): Game = {
			return new Game(List((3,0),(3,1),(3,2)), List((4,1, score =>score + 5),(3,3, score => score + 10)), 4, 1)
	}
}