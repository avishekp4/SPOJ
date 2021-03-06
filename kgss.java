import java.io.*;
import java.util.*;
import static java.lang.Math.max;

public class MaximumSum
{
    private static Reader in;
    private static PrintWriter out;
    private static int [] arr;

    public static void main (String [] args) throws IOException {
        in = new Reader ();
        out = new PrintWriter (System.out, true);
        int n = in.nextInt ();
        arr = new int [n + 1];
        for (int i = 1; i <= n; i++) {
            arr[i] = in.nextInt();
        }
        IntervalTree root = new IntervalTree (1, n);
        int t = in.nextInt ();
        while (t-- > 0) {
        	char command = in.nextChar();
            int 	a = in.nextInt(),
            		b = in.nextInt();
            if (command == 'Q') {
            	IntervalTree result = root.query(a, b);
            	out.println(result.max + result.secondMax);
            } else if (command == 'U'){
            	//arr[0] = 3;
            	root.update(a, b);
            }
        }
    }
    
    static class IntervalTree {
        public IntervalTree Lchild = null, Rchild = null;
        public int max, secondMax, start, end;
        
        public IntervalTree () {}
        
        public IntervalTree (int _start, int _end) {
            start = _start; end = _end;
            if (start != end) {
                int mid = (start + end) >> 1;
                Lchild = new IntervalTree (start, mid);
                Rchild = new IntervalTree (mid + 1, end);
                join (this, Lchild, Rchild);
            }
            else {
            	this.max = arr [start];
            	this.secondMax = -1;
            }
        }
        
        public IntervalTree query (int a, int b) {
            if (a == start && end == b) return this;
            int mid = (start + end) >> 1;
            if (a > mid) return Rchild.query (a, b);
            if (b <= mid) return Lchild.query (a, b);
            IntervalTree ans = new IntervalTree ();
            join (ans, Lchild.query (a, mid), Rchild.query (mid + 1, b));
            return ans;
        }
        
        public void update(int i, int x) {
            if(i == start && i == end) {
                arr[i] = x;
                this.max = x;
                this.secondMax = -1;
                return; 
            }
            if(start > i || i > end) return;
            if(Lchild == null) return;
            int mid = (start + end) >> 1;
            if (i > mid) Rchild.update(i, x);
            else         Lchild.update(i, x);
            join (this, Lchild, Rchild);               
        }
        
        public void join (IntervalTree parent, IntervalTree Lchild, IntervalTree Rchild) {
            parent.max = Math.max(Lchild.max, Rchild.max);
            parent.secondMax = Math.max(Math.max(Lchild.secondMax, Rchild.secondMax), Math.min(Lchild.max, Rchild.max));
        }
    }
}

/** Faster input **/
class Reader {
    final private int BUFFER_SIZE = 1 << 16;
    private DataInputStream din;
    private byte[] buffer;
    private int bufferPointer, bytesRead;
    public Reader(){
        din=new DataInputStream(System.in);
        buffer=new byte[BUFFER_SIZE];
        bufferPointer=bytesRead=0;
    }

    public Reader(String file_name) throws IOException{
        din=new DataInputStream(new FileInputStream(file_name));
        buffer=new byte[BUFFER_SIZE];
        bufferPointer=bytesRead=0;
    }

    public String readLine() throws IOException{
        byte[] buf=new byte[64]; // line length
        int cnt=0,c;
        while((c=read())!=-1){
            if(c=='\n')break;
            buf[cnt++]=(byte)c;
        }
        return new String(buf,0,cnt);
    }

    public int nextInt() throws IOException{
        int ret=0;byte c=read();
        while(c<=' ')c=read();
        boolean neg=(c=='-');
        if(neg)c=read();
        do{ret=ret*10+c-'0';}while((c=read())>='0'&&c<='9');
        if(neg)return -ret;
        return ret;
    } 

    public long nextLong() throws IOException{
        long ret=0;byte c=read();
        while(c<=' ')c=read();
        boolean neg=(c=='-');
        if(neg)c=read();
        do{ret=ret*10+c-'0';}while((c=read())>='0'&&c<='9');
        if(neg)return -ret;
        return ret;
    }

    public double nextDouble() throws IOException{
        double ret=0,div=1;byte c=read();
        while(c<=' ')c=read();
        boolean neg=(c=='-');
        if(neg)c = read();
        do {ret=ret*10+c-'0';}while((c=read())>='0'&&c<='9');
        if(c=='.')while((c=read())>='0'&&c<='9')
            ret+=(c-'0')/(div*=10);
        if(neg)return -ret;
        return ret;
    }
    
    public char nextChar() throws IOException{
        byte c=read();
        while(c<=' ')c=read();
        return (char)c;
    }
    
    private void fillBuffer() throws IOException{
        bytesRead=din.read(buffer,bufferPointer=0,BUFFER_SIZE);
        if(bytesRead==-1)buffer[0]=-1;
    }
    
    private byte read() throws IOException{
        if(bufferPointer==bytesRead)fillBuffer();
        return buffer[bufferPointer++];
    }
    
    public void close() throws IOException{
        if(din==null) return;
        din.close();
    }
}
