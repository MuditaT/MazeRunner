package mazeRunner;

public class GA {
	
	private static int len;
	private int pop;
	private int gen;
	private int max_fitness;
	
	
	
	public GA() {
		super();
	}
	public GA(int len, int pop, int gen) {
		GA.len = len*len - 2;
		this.pop = pop;
		this.gen = gen;
		
	}

	public static int getLen() {
		return len;
	}

	public static void setLen(int len) {
		GA.len = len;
	}

	

	public  int[][] genetic(int mode) {
	
		FitnessCalc.mode = mode;
        // Create an initial population
        Population myPop = new Population(this.pop, true);
        
        // Evolve our population until we reach an optimum solution
        int generationCount = 0;
       // while (myPop.getFittest().getFitness() < FitnessCalc.getMaxFitness()) {
        while(generationCount < this.gen) {
            generationCount++;
            this.max_fitness = myPop.getFittest().getFitness();
            System.out.println("Generation: " + generationCount + " Fittest: " + this.max_fitness);
            myPop = Algorithm.evolvePopulation(myPop);
        }
        if (this.max_fitness > 0) {
        System.out.println("Solution found!");
        System.out.println("Generation: " + generationCount);
        System.out.println("Genes:");
        int[] g = myPop.getFittest().genes;
        int size = (int) Math.sqrt(g.length + 2);
        int b[] = new int[g.length + 2];
		b[0] = 0;
		b[g.length + 1] = 9;
		int j = 1;
		for (int i = 0; i < g.length; i++) {
			b[j++] = g[i];
		}
		int maze[][] = new int[size][size];

		int k = 0;
		for (int m = 0; m < size; m++) {
			for (int r = 0; r < size; r++) {
					maze[m][r] = b[k++];
				}
			}
		
        for (int i = 0; i < size; i++) {
			for (int x = 0; x < size; x++) {
				System.out.print(maze[i][x]);
			}
			System.out.println(" ");
        }
        System.out.println("------");
		return maze;
        }
        
        return null;


	}

}
