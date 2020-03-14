import java.util.Arrays;
import java.util.HashSet;

class LZWencode
{
    int BUFFER_SIZE=256;
        //trie class
    
        static int count=-1;
     static byte[] string=null;
            static String code="";
            //node class
            class trie_node
            {
                //root node
            Boolean isRoot=false;
            trie_node root;
            byte[] dict;
            

            int dict_size;

                trie_node[] children ;
                byte value;
                int level;

                public trie_node(byte ch)
                {
                    children = new trie_node[BUFFER_SIZE];
                    level=count;
                    value=ch;
                }
                public trie_node()
                {
                    children = new trie_node[BUFFER_SIZE];
                    level=count;
                   
                }
            

            
            //constructor 
           public trie_node(byte[] input)
            {
                isRoot=true;
                                dict_size=0;
                                Arrays.sort(input);
                                dict= new byte[256];
                                root = new trie_node();
                                count++;
                                //remove dulplicates from the array
                                for(int j=0;j<input.length;j++)
                                {
                                    for(int i=0;i<input.length;i++)
                                    {
                                    
                                        if(input[j]==input[i] && i!=j)
                                        {
                                            input[i]='\0';
                                        }
                                        
                                    }
                                }
                                int x=0;
                                for(int i=0;i<BUFFER_SIZE;i++)
                                {
                                    dict[i]=0;

                                }
                                for (int i=0;i<input.length;i++) {
                                    if(input[i]!=0)
                                    {
                                        dict[x]=input[i];
                                        x++;
                                    }
                                    
                                }
                            
                            for(int i=0;i<dict.length;i++)
                            {
                                if(dict[i]!=0)
                                {
                                    root.children[i]= new trie_node(dict[i]);
                                    
                                    count++;
                                }
                                    
                            }
              
                               
            }

                            public void display_dictionary()
                            { String a;
                            for(int i=0;i<dict.length;i++)
                            {
                                
                                
                                if(root.children[i]!=null)
                                {
                                    System.out.println(String.valueOf((char)(root.children[i].value))+" = "+root.children[i].level);
                                }
                            }
                            System.out.println("Size = "+count);
                                
                            }

                
                public void encode(byte[] input)
                {
                    
                    System.out.println(new String(input));
                  string = input;
                while(string.length>0)
                   find(root,string);
                 
                    
                   System.out.println(code);
                  
                   
                    


                }
                
                
                
                public  void find(trie_node node,byte[] str)
                {
                    
                
                    
                            if(str.length>0)
                            {
                                byte curr = str[0];
                                
                                for(int i=0;i<node.children.length;i++)
                                {
                                    if( node.children[i]!=null && curr==node.children[i].value)
                                    {
                                        string=sub(str);
                                       
                                         find(node.children[i],string);
                                         
                                         return;
                                        
                                    }   

                                }
                                add(node,curr);
                                       
                                       return ;
                                    
                            }
                            else
                            {
                                code+= Integer.toString(node.level);
                                return;
                                
                            }
                           
                }         
                
                public void add(trie_node node, byte curr)
                {
                    for(int i=0;i<node.children.length;i++)
                    {
                        if(node.children[i]==null)
                        {
                            node.children[i]= new trie_node(curr);
                           
                            count++;
                            code+= Integer.toString(node.level);
                          
                            break;
                        }
                    }
                    return;

                }
                   
            

               
               public byte[] sub(byte[] str)
               {
                   
                byte[] st = new byte[str.length-1];
                for(int i=1;i<str.length;i++)
                    st[i-1]=str[i];
                    return st;
                  
               
               }

            }






public static void main(String[] args)
{
    LZWencode  lz = new LZWencode();
    lz.run(args);
    


}
public void run(String[] args)
{
    byte[] input = args[0].getBytes();
    trie_node tn = new trie_node(input);
    tn.display_dictionary();
    tn.encode(args[0].getBytes());


}





}