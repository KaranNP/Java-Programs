import java.awt.*;
import java.awt.event.*;
class WindowCloser extends WindowAdapter
{
	public void windowClosing(WindowEvent e)
	{
		System.out.print("hi");
		Window w = e.getWindow();
		w.setVisible(false);
		w.dispose();
		System.exit(1);
	}
}
class CalcView implements ActionListener 
{
	Button b1,b2,b3,b4,b5,b6,b7,b8,b9,b0,bc,bd,be,bp,bm,bs;
	TextField t;
	Panel p1;
	int i=0;
	double sum,num1,num2;
	double a,b;
	String s1=null,s2;
	
	public CalcView()
	{
		Frame f=new Frame();
		f.setSize(400,300); // Width x Height
		t = new TextField();
		p1 = new Panel();
		GridLayout gl1= new GridLayout(4,4);
		p1.setLayout(gl1);
		b1 = new Button("1");
		b2 = new Button("2");
		b3 = new Button("3");
		bp = new Button("+");
		b4 = new Button("4");
		b5 = new Button("5");
		b6 = new Button("6");
		bm = new Button("-");
		b7 = new Button("7");
		b8 = new Button("8");
		b9 = new Button("9");
		bs = new Button("*");
		bc = new Button("c");
		b0 = new Button("0");
		bd = new Button("/");
		be = new Button("=");
		
		b1.addActionListener(this);
		b2.addActionListener(this);
		b3.addActionListener(this);
		bp.addActionListener(this);
		b4.addActionListener(this);
		b5.addActionListener(this);
		b6.addActionListener(this);
		bm.addActionListener(this);
		b7.addActionListener(this);
		b8.addActionListener(this);
		b9.addActionListener(this);
		bs.addActionListener(this);
		bc.addActionListener(this);
		b0.addActionListener(this);
		bd.addActionListener(this);
		be.addActionListener(this);
		
		p1.add(b1);
		p1.add(b2);
		p1.add(b3);
		p1.add(bp);
		p1.add(b4);
		p1.add(b5);
		p1.add(b6);
		p1.add(bm);
		p1.add(b7);
		p1.add(b8);
		p1.add(b9);
		p1.add(bs);
		p1.add(bc);
		p1.add(b0);
		p1.add(bd);
		p1.add(be);
		
		WindowCloser wc = new WindowCloser();
		f.addWindowListener(wc);
		f.add(p1);
		f.add(t,"North");
		f.setVisible(true);		
	}
	
	public void actionPerformed(ActionEvent e)
	{
		String str=e.getActionCommand();
		
		if(((str=="+")||(str=="-")||(str=="*")||(str=="/")||(str=="=")||(str=="c"))&&(s1==null))
		{
			num1=b;
			b=0;
			s1=str;
			t.setText(str);
			if(s1=="=")
				t.setText(num1+"");
			if(s1=="c")
			{
				t.setText("");
				s1=null;
				s2="";
				num1=0;
				num2=0;
			}
			System.out.print("First");
		}
		else
		if(((str=="+")||(str=="-")||(str=="*")||(str=="/")||(str=="=")||(str=="c"))&&(s1!=null))
		{
			System.out.print("Two");
			num2=b;
			s2=str;
			b=0;
			if(s1=="+")
			{
				num1=num1+num2;
				
				//s1=s2;
			}
			if(s1=="-")
			{
				num1=num1-num2;
				//t.setText(num1+"");	
				//s1=s2;
			}
			if(s1=="*")
			{
				num1=num1*num2;
				//t.setText(num1+"");	
				//s1=s2;
			}
			if(s1=="/")
			{
				num1=num1/num2;
				//t.setText(num1+"");	
				//s1=s2;
			}
			if(s1=="=")
			{
				t.setText(num1+"");	
			//	s1=s2;
				
			}
			t.setText(num1+"");	
			s1=s2;
			if(s1=="c")
			{
				t.setText("");
				s1=null;
				s2="";
				num1=0;
				num2=0;
			}
		}
		else
		{
				a=Double.parseDouble(str);
				b=b*10+a;
				t.setText(b+"");
		}			
	}
	public static void main(String arg[])
	{
		CalcView c = new CalcView();
	}
}
		
		