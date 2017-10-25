package mazeRunner;

public class FitnessCalc {

	
	 static byte[] solution = new byte[79];
	 
	 public static int mode;
	 
	 


	    static int getFitness(Individual individual) {
	        
	        int a[] = individual.genes; // input 2d array
//	        
//	        for(int i= 0; i<a.length;i++) {
//	        System.out.println(a[i]);
//	        }

			int size = (int) Math.sqrt(a.length + 2);

			int b[] = new int[a.length + 2];
			b[0] = 0;
			b[a.length + 1] = 9;
			int j = 1;
			for (int i = 0; i < a.length; i++) {
				b[j++] = a[i];
			}

			//int maze[][] = new int[size + 2][size + 2];
			int maze[][] = new int[size][size];

			int k = 0;
			for (int i = 0; i < size; i++) {
				for (j = 0; j < size; j++) {
//					if (i == 0 || i == size + 1 || j == 0 || j == size + 1) {
//						maze[i][j] = 1;
//					}
//					else
//					{
						maze[i][j] = b[k++];
					}
				}
			

//			for (int i = 0; i < size; i++) {
//				for (j = 0; j < size; j++) {
//					System.out.print(maze[i][j]);
//				}
//				System.out.println(" ");
//								
//			}
//			System.out.println(" ");

			
			Maze1 m = new Maze1(maze);
			
			
			int sum = 0;
			
			for(int i = 0; i < 4 ; i++) {
				//System.out.println(i);
				//System.out.println(m.getPathLength(i));
				if(mode==0) {
				sum += m.getPathLength(i);
				}
				else if(mode==1) {
				sum += m.getNodes(i);
				}
				else {
				sum += m.getFringeLength(i);
				}
			}
			return sum;

	    }
	    
	  
}
