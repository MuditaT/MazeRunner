package mazeRunner;

import java.util.*;



public class Maze1 {
	
	
	 public  int[][] arr;
	 public  int[][] abfs;
	 public  int[][] adfs;
	 public int[][] as;
	 public int[][] ase;
	 public  int len;
	 public  int[][] om;
	 
	 
	 public int bfs_length;
	 public int dfs_length;
	 public int a_length;
	 public int as_length;
	 
	 public int bfs_f;
	 public int dfs_f;
	 public int a_f;
	 public int as_f;
	 
	 
	 
	

	public Maze1(int[][] a) {
        this.arr = a;
        this.len = arr.length;
        this.adfs = new int[this.len][this.len];
        this.abfs = new int[this.len][this.len];
        this.as = new int[this.len][this.len];
        this.ase = new int[this.len][this.len];
        
        
        for(int i = 0; i< this.len;i++) {
        	 for (int j=0;j< this.len;j++) {
        		 this.adfs[i][j] = this.arr[i][j];
        	 }
        }
        for(int i = 0; i< this.len;i++) {
       	 for (int j=0;j< this.len;j++) {
       		 this.abfs[i][j] = this.arr[i][j];
       	 }
       }      
        for(int i = 0; i< this.len;i++) {
          	 for (int j=0;j< this.len;j++) {
          		 this.as[i][j] = this.arr[i][j];
          	 }
          } 
        for(int i = 0; i< this.len;i++) {
         	 for (int j=0;j< this.len;j++) {
         		 this.ase[i][j] = this.arr[i][j];
         	 }
         } 
        
	}
    

  
  public  int bfs_nodes;
  public  int dfs_nodes;
  public int as_nodes;
  public int ase_nodes;
  
  


  private  class Point {
        int x;
        int y;
        Point parent;
        @Override
        public boolean equals(Object o) {
        	
        	Point q = (Point)o;
        	      if(this.x == q.x && this.y == q.y) {
        	    	  
        	    	  return true;
        	    	  
        	      }
        	      else {
        	    	  return false;
        	      }
        }

        public Point(int x, int y, Point parent) {
            this.x = x;
            this.y = y;
            this.parent = parent;
        }

        public Point getParent() {
            return this.parent;
        }

        public String toString() {
            return "x = " + x + " y = " + y + " Parent = (" + parent.x + "," + parent.y + ")";
        }
  }
  
  public int getCost(Point p) {
	  //System.out.println("entered get cost");
	  int i = 0;
	  while(p.getParent() != null) {
		  p = p.getParent();
		  i += 1;
		  
	  }
	  //System.out.println(i);
	  return i;
  }
  
  private class manComp implements Comparator<Point> {
	  @Override 
	  public int compare(Point p, Point q)
	  
	    {
		  //System.out.println(p+"--"+q);
		  
		    int  a = Math.abs(p.x-len+1) + Math.abs(p.y-len+1) ;
		    int c = getCost(p);
		    int b = Math.abs(q.x-len+1) + Math.abs(q.y-len+1) ;
		    int d = getCost(q);
//		    System.out.println(a+"---"+c);
//		    System.out.println(b+"---"+d);
//		    System.out.println("---");
	        return (a+c)-(b+d);
	    }
		
  }
  private class euComp implements Comparator<Point> {
	  @Override 
	  public int compare(Point p, Point q)
	    {
		  
		  int  a = (int)Math.sqrt(Math.pow(Math.abs(p.x-len+1),2) + Math.pow(Math.abs(p.y-len+1),2)) + getCost(p);
		    int b = (int)Math.sqrt(Math.pow(Math.abs(q.x-len+1),2) + Math.pow(Math.abs(q.y-len+1),2)) + getCost(q);
		    //System.out.println(a+"---"+b);
		    //System.out.println("---");
	        return a-b;
	    }
		
  }
  
  
  public  Queue<Point> q = new LinkedList<Point>();
  
  public  Deque<Point> stack = new ArrayDeque<Point>();
  
  
  
  public Point getPathA(int x,int y) { 
	  
	  this.as_nodes = 0;
	  this.a_f = 0;
	  Comparator<Point> comparator;
	   comparator = new manComp();
	 
      PriorityQueue<Point> queue = new PriorityQueue<Point>((int)Math.pow(this.len,2), comparator);
      
      queue.add(new Point(x,y,null));
      
     
      
      while(!queue.isEmpty()) {
    	  //System.out.println(queue);
    	  
    	  if (queue.size() >this.a_f) {
    		  this.a_f = queue.size();
    	  }
    	  
    	  Point p = queue.remove();
    	  
    	  
    	  
//   	  for (int i = 0; i < this.len; i++) {
//				for (int j = 0; j < this.len; j++) {
//				System.out.print(as[i][j]);
//				}
//				System.out.println(" ");
//								
//			}
//    	      System.out.println(queue);
    	  
    	  
    	  if (isFree(p.x,p.y,as) & (as[p.x][p.y] != 9)) {
    		  
    		 // System.out.println(p.x+","+p.y);
        	  
        	  //System.out.println("entered first:");
        	  as[p.x][p.y] = 3;
          this.as_nodes += 1;
          //System.out.println(this.dfs_nodes);
          }

          if (as[p.x][p.y] == 9) {
              //System.out.println("Exit is reached!");
              return p;
          }
          if(isFree(p.x+1,p.y,as)) { 
        	  //System.out.println("entered x+1");
              Point nextP = new Point(p.x+1,p.y, p);
              if(!queue.contains(nextP)) {
//            	  System.out.println("entered not contains X+1 block");
//            	  System.out.println(queue);
//            	  System.out.println(nextP);
            	  
            	 
            	  
            	  
              queue.add(nextP);
              }
          }
          if(isFree(p.x,p.y+1,as)) {
        	  //System.out.println("entered y+1");
              Point nextP = new Point(p.x,p.y+1, p);
              if(!queue.contains(nextP)) {
//            	  System.out.println("entered not contains y+1 block");
//            	  System.out.println(queue);
//            	  System.out.println(nextP);
            	  
                  queue.add(nextP);
                  }
              
          }
          
          
          if(isFree(p.x,p.y-1,as)) {
        	  //System.out.println("entered y-1");
              as[p.x][p.y] = 3;
              Point nextP = new Point(p.x,p.y-1, p);
              if(!queue.contains(nextP)) {
//            	  System.out.println("entered not contains y-1 block");
//            	  System.out.println(queue);
//            	  System.out.println(nextP);
            	  
                  queue.add(nextP);
                  }
              
          }
          if(isFree(p.x-1,p.y,as)) {
        	  //System.out.println("entered x-1");
              Point nextP = new Point(p.x-1,p.y, p);
              if(!queue.contains(nextP)) {
//            	  System.out.println("entered not contains x-1  block");
//            	  System.out.println(queue);
//            	  System.out.println(nextP);
            	  
                  queue.add(nextP);
                  }
              
          }
    	  
    	  
    	  
      }
      
      return null;
      
  }

public Point getPathAe(int x,int y) { 
	  
	  this.ase_nodes = 0;
	  this.as_f = 0;
	  Comparator<Point> comparator;
	   comparator = new euComp();
	 
      PriorityQueue<Point> queue = new PriorityQueue<Point>((int)Math.pow(this.len,2), comparator);
      
      queue.add(new Point(x,y,null));
      
      //System.out.println(queue);
      
      while(!queue.isEmpty()) {
    	  //System.out.println(queue);
    	  
    	  if (queue.size() >this.as_f) {
    		  this.as_f = queue.size();
    	  }
    	  
    	  Point p = queue.remove();
    	  
    	  
    	  
//   	  for (int i = 0; i < this.len; i++) {
//				for (int j = 0; j < this.len; j++) {
//				System.out.print(as[i][j]);
//				}
//				System.out.println(" ");
//								
//			}
//    	      System.out.println(q);
//    	  
    	  
    	  if (isFree(p.x,p.y,ase) & (ase[p.x][p.y] != 9)) {
        	  
        	  //System.out.println("entered first:");
        	  ase[p.x][p.y] = 3;
          this.ase_nodes += 1;
          //System.out.println(this.dfs_nodes);
          }

          if (ase[p.x][p.y] == 9) {
              //System.out.println("Exit is reached!");
              return p;
          }
          if(isFree(p.x+1,p.y,ase)) { 
        	  //System.out.println("entered x+1");
              Point nextP = new Point(p.x+1,p.y, p);
              if(!queue.contains(nextP)) {
            	  		queue.add(nextP);
                  }
              
          }
          if(isFree(p.x,p.y+1,ase)) {
        	  //System.out.println("entered y+1");
              Point nextP = new Point(p.x,p.y+1, p);
              if(!queue.contains(nextP)) {
      	  		queue.add(nextP);
            }
              
          }
          
          
          if(isFree(p.x,p.y-1,ase)) {
        	  //System.out.println("entered y-1");
              ase[p.x][p.y] = 3;
              Point nextP = new Point(p.x,p.y-1, p);
              if(!queue.contains(nextP)) {
      	  		queue.add(nextP);
            }
              
          }
          if(isFree(p.x-1,p.y,ase)) {
        	  //System.out.println("entered x-1");
              Point nextP = new Point(p.x-1,p.y, p);
              if(!queue.contains(nextP)) {
      	  		queue.add(nextP);
            }
          }
    	  
    	  
    	  
      }
      
      return null;
      
  }


  public  Point getPathBFS(int x, int y) {
	  
	  
	  this.bfs_nodes = 0;
	  this.bfs_f = 0;
      q.add(new Point(x,y, null));

      while(!q.isEmpty()) {
    	      
    	  if (q.size() >this.bfs_f) {
    		  this.bfs_f = q.size();
    	  }
          Point p = q.remove();
//   	  for (int i = 0; i < this.len; i++) {
//				for (int j = 0; j < this.len; j++) {
//				System.out.print(abfs[i][j]);
//				}
//				System.out.println(" ");
//								
//			}
//    	      System.out.println(q);
    	  
   
          
          if (isFree(p.x,p.y,abfs) & (abfs[p.x][p.y] != 9)) {
        	  
        	  //System.out.println("entered:");
        	  abfs[p.x][p.y] = 3;
          this.bfs_nodes += 1;
          //System.out.println(this.dfs_nodes);
          }

          if (abfs[p.x][p.y] == 9) {
              //System.out.println("Exit is reached!");s
              return p;
          }
          if(isFree(p.x+1,p.y,abfs)) { 
              Point nextP = new Point(p.x+1,p.y, p);
              if(!q.contains(nextP)) {
      	  		q.add(nextP);
            }
          }
          if(isFree(p.x,p.y+1,abfs)) {
              Point nextP = new Point(p.x,p.y+1, p);
              if(!q.contains(nextP)) {
        	  		q.add(nextP);
              }
          }
          
          
          if(isFree(p.x,p.y-1,abfs)) {
              abfs[p.x][p.y] = 3;
              Point nextP = new Point(p.x,p.y-1, p);
              if(!q.contains(nextP)) {
        	  		q.add(nextP);
              }
          }
          if(isFree(p.x-1,p.y,abfs)) {
        	  
              Point nextP = new Point(p.x-1,p.y, p);
              if(!q.contains(nextP)) {
        	  		q.add(nextP);
              }
          }
          
    
      }
      return null;
  }

  public  Point getPathDFS(int x, int y) {
	  
      this.dfs_nodes = 0;
      this.dfs_f = 0;
      stack.push(new Point(x,y, null));
      while(!stack.isEmpty()) {
    	  
    	  if(stack.size() > this.dfs_f) {
    		  this.dfs_f = stack.size();
    	  }
    	  
//   	  for (int i = 0; i < this.len; i++) {
//				for (int j = 0; j < this.len; j++) {
//				System.out.print(adfs[i][j]);
//				}
//				System.out.println(" ");
//								
//			}
//    	      System.out.println(stack);
//    	  
//    	  
    	  
          Point p = stack.pop();
          if (isFree(p.x,p.y,adfs) & (adfs[p.x][p.y] != 9)) {
        	  
        	  //System.out.println("entered:");
        	  adfs[p.x][p.y] = 3;
          this.dfs_nodes += 1;
          //System.out.println(this.dfs_nodes);
          }
          
          //System.out.println("popped element");
          //System.out.println(p);

          if (adfs[p.x][p.y] == 9) {
              //System.out.println("Exit is reached!");
              //System.out.println(dfs_nodes);
              return p;
              
          }
          if(isFree(p.x,p.y-1,adfs)) {
              
              Point nextP = new Point(p.x,p.y-1, p);
              if(stack.contains(nextP)) {
            	  stack.remove(nextP);
              }
              stack.push(nextP);
          }

          if(isFree(p.x-1,p.y,adfs)) {
              
              Point nextP = new Point(p.x-1,p.y, p);
              if(stack.contains(nextP)) {
            	  stack.remove(nextP);
              }
              stack.push(nextP);
          }
         
          if(isFree(p.x,p.y+1,adfs)) {
        	  //System.out.println(" y+1 entered");
              
              Point nextP = new Point(p.x,p.y+1, p);
              if(stack.contains(nextP)) {
            	  stack.remove(nextP);
              }
              stack.push(nextP);
          }
          if(isFree(p.x+1,p.y,adfs)) {
       	     //System.out.println(" x+1 entered");
             
             Point nextP = new Point(p.x+1,p.y, p);
             if(stack.contains(nextP)) {
           	  stack.remove(nextP);
             }
             stack.push(nextP);
         }
         
         

          

          

          
      }
      return null;
  }

  public static boolean isFree(int x, int y, int[][] a) {
      if((x >= 0 && x < a.length) && (y >= 0 && y < a[x].length) && (a[x][y] == 0 || a[x][y] == 9)) {
          return true;
      }
      return false;
  }
  
  public  void printMaze(Point p, String s) {
	  
	  ArrayList<ArrayList<Integer>> a = new ArrayList<ArrayList<Integer>>();
	  
	  int[][] ar;
	  if(s.equals("bfs")) {
	  
	  ar = abfs;
	  }
	  else if(s.equals("dfs")) {
	  ar = adfs;
	  }
	  else if(s.equals("as")){
		  ar = as;
	  }
	  else {
		  ar=ase;
	  }
	  
	  int l = 0;
      if(p == null) {
    	  System.out.println("no path exists");
    	  return;
      }
      while(p.getParent() != null) {
          //System.out.println(p);
          p = p.getParent();
          ArrayList<Integer> b = new ArrayList<Integer>();
          
          b.add(p.x);
          b.add(p.y);
          
          a.add(b);
          l = l+1;
      }

       
       for (int i = 0; i < len; i++) {
          for (int j = 0; j < len; j++) {
        	  
        	   ArrayList<Integer> c = new ArrayList<Integer>();
        	   c.add(i);
        	   c.add(j);
        	   
        	   if(a.contains(c)) {
        		   
        		   ar[i][j] = 8;
        		   
        	   }
        	  
              System.out.print(ar[i][j]+" ");
          }
          System.out.println();
      }
       //System.out.println(stack);
	  
  }

  public static void main(String[] args) {
	  
//	  for(int k =  0; k < arr.length; k++) 
//	  {
//		  for(int j=0; j< arr[0].length; j++) 
//		  {
//		    abfs[k][j]=arr[k][j];
//		  }
//	  }
//	  
//	  for(int i=0; i<arr.length; i++) {
//		  for(int j=0; j<arr[0].length; j++) {
//		    adfs[i][j]=arr[i][j];
//		  }
//	  }
//
//	  
//      printMaze(getPathBFS(0,0),"bfs");
//      printMaze(getPathDFS(0,0),"dfs");
      
      
  }
  
  public int getPathLength(int i) {
	  
	  Point p;
	  
	  if(i==0) {
		   p = getPathDFS(0,0);
	  }
	  else if(i==1) {
		  p = getPathBFS(0,0);
		  
	  }
	  else if(i==2) {
		  p = getPathA(0,0);
	  }
	  else {
		  p = getPathAe(0,0);
	  }
	  
	  int j = 0;
	  if(p==null) {
		  return 0;
	  }
	  while(p.getParent() != null) {
		  p = p.getParent();
		  j += 1;
		  
	  }
	  return j;
	  
	  
	  
  }
  
  public int getFringeLength(int i) {
	  if(i==0) {
		  Point p = getPathDFS(0,0);
		  if(p == null) {
			  return 0;
		  }
		  return this.dfs_f;
	  }
	  else if(i==1) {
		  Point p = getPathBFS(0,0);
		  if(p == null) {
			  return 0;
		  }
		  return this.bfs_f;
		  
	  }
	  else if(i==2) {
		  Point p = getPathA(0,0);
		  if(p == null) {
			  return 0;
		  }
		  return this.a_f;
	  }
	  else {
		  Point p = getPathAe(0,0);
		  if(p == null) {
			  return 0;
		  }
		  return this.as_f;
	  }
  }
  
  public int getNodes(int i) {
	  
	 if (i == 0){
	  Point p = getPathDFS(0,0);
	  //System.out.println("dfs nodes" + this.dfs_nodes);
	  //printMaze(p,"dfs");
	  
	  	  if (p == null) {
		  return 0;
	  }
	  	  
//	  	  if (dfs_nodes > 25) {
//	  		  System.out.println("Input:");
//	  		for (int i = 0; i < this.len; i++) {
//				for (int j = 0; j < this.len; j++) {
//					System.out.print(arr[i][j]);
//				}
//				System.out.println(" ");
//								
//			}
//			System.out.println(" ");
//			System.out.println("Output:");
//	  		printMaze(p , "dfs");
//	  		System.out.println("nodes:" + dfs_nodes);
//	  		System.out.println("---------");
//
//	  	  }
	  return this.dfs_nodes;
	 }
	 else if(i==1){
		 Point p = getPathBFS(0,0);
		 //printMaze(p,"bfs");
		 if(p==null) {
			 return 0;
		 }
		 return this.bfs_nodes;
	 }
	 else if(i==2)
	  {
		 Point p = getPathA(0,0);
		 //printMaze(p,"as");
		 if(p == null){
			 return 0;
		 }
		 return this.as_nodes;
	
  }
	 else {
		 Point p = getPathAe(0,0);
		 //printMaze(p,"ase");
		 if(p == null){
			 return 0;
		 }
		 return this.ase_nodes;
	 }

}
}

