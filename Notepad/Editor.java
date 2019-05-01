import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Editor extends WindowAdapter implements ActionListener,TextListener
{
	Frame f;
	MenuBar mb;
	Menu m1,m2;
	MenuItem nw,opn,sve,svea,ext,fnd,fndrep;
	TextArea txt;
	
	SavePrompt sp;
	Find findbox;
	FindReplace findreplacebox;
	NoMatch nomatchfound;
	
	String file_path;  // stores path of current file
	String file_content; //stores content of textarea 
	String default_path; // stores path of untitled file
	boolean isCancel = true; // In save prompt,if cancelled is clicked then true.
	boolean firstrun = true;
	
	Matcher m;
	Pattern p;
	
	public Editor()
	{
		f = new Frame();
		f.setSize(800,700);
		
		mb = new MenuBar();
		m1 = new Menu("File");
		m2 = new Menu("Edit");
		
		nw = new MenuItem("New");
		opn = new MenuItem("Open");
		sve = new MenuItem("Save");
		svea = new MenuItem("Save As");
		ext = new MenuItem("Exit");
		
		fnd = new MenuItem("Find");
		fndrep = new MenuItem("Find and Replace");
		
		nw.addActionListener(this);
		opn.addActionListener(this);
		sve.addActionListener(this);
		ext.addActionListener(this);
		svea.addActionListener(this);
		
		fnd.addActionListener(this);
		fndrep.addActionListener(this);
		
		txt = new TextArea();
		
		txt.addTextListener(this);
		
		m1.add(nw);
		m1.add(opn);
		m1.add(sve);
		m1.add(svea);
		m1.add(ext);
		m2.add(fnd);
		m2.add(fndrep);
		
		mb.add(m1);
		mb.add(m2);
		
		f.add(txt);
		f.addWindowListener(this);

		f.setMenuBar(mb);
		
		try
		{
			File file = new File("temp");
			String str;
			if(!file.isDirectory())
				file.mkdir();
			str=file.getAbsolutePath();
			str=str+"\\untitled.txt";
			f.setTitle("untitled.txt");
			FileOutputStream fos = new FileOutputStream(str);			//File named "untitled" is created
			file_path = str;
			default_path = str;
			file_content="";			
		}
		catch(Exception e1)
		{
			System.out.print("Exception in creating default file \t"+e1.getMessage());
		}			
		
		sp = new SavePrompt();
		findbox = new Find();
		findreplacebox = new FindReplace();
		nomatchfound = new NoMatch();
		
		fnd.setEnabled(false);
		fndrep.setEnabled(false);
		
		f.setVisible(true);		
	}
	public void windowClosing(WindowEvent e)
	{
		fexit2();
	}
	
	public void actionPerformed(ActionEvent e)
	{
		Object ae=e.getSource();
		
		
		if(ae==nw)
		{
			System.out.println("New is clicked");
			fnew2();
		}
		if(ae==opn)
		{
			System.out.println("Open is clicked");
			fopen2();
		}
		if(ae==sve)
		{
			System.out.println("Save is clicked");
			fsave();
		}
		if(ae==svea)
		{
			System.out.println("Save As is clicked");
			fsaveas();
		}
		if(ae==ext)
		{
			System.out.println("Exit is clicked");
			fexit2();
		}
		if(ae==fnd)
		{
			System.out.println("Find is clicked");
			findbox.show();
		}
		if(ae==fndrep)
		{
			System.out.println("Find and Replace is clicked");
			findreplacebox.show();
		}			
	}
	public void fexit2()
	{
		if(isSaved())
			fexit();
		else
		{
			sp.openAskSaveDialog();
			if(!isCancel)
				fexit();
		}
	}
	
	public void fexit()
	{
		f.setVisible(false);
		f.dispose();
		System.exit(1);
	}
	
	public void fopen2()
	{
		if(isSaved())
		{
			fopen();
		}
		else
		{
			sp.openAskSaveDialog();
			if(!isCancel)
				fopen();
		}
	}
	
	public void fnew2()
	{
		if(isSaved())
		{
			fnew();
		}
		else
		{
			sp.openAskSaveDialog();
			if(!isCancel)
				fnew();
		}
	}
	
	public void fopen()
	{
		//1. Check if file is saved or not
		FileDialog fd = new FileDialog(f,"Open file",FileDialog.LOAD);
		fd.setVisible(true);
		String file_name = fd.getFile();
		String dir_name = fd.getDirectory();
		//System.out.println("File location:\n"+dir_name+"\\"+file_name);
		File opened_file = new File(dir_name+"\\"+file_name);
		System.out.println(opened_file.getAbsolutePath());
		if(file_name!=null)
		{
			try
			{
				FileInputStream fis = new FileInputStream(opened_file);				
				int ch;
				String stropen="";
				while((ch=fis.read())!=-1)
				{
					stropen = stropen + (char)ch;
				}
				//System.out.println(stropen);
				txt.setText(stropen);
				f.setTitle(file_name);
				file_path = opened_file.getAbsolutePath();
				file_content = stropen;
			}
			catch(Exception e1)
			{
				System.out.print("Exception at fopen \t"+e1.getMessage());
			}
			
		}
		else
		{
			System.out.println("Cancel was clicked");
		}
	}
	
	public void fsaveas()
	{
		FileDialog fd = new FileDialog(f,"Save file",FileDialog.SAVE);
		fd.setVisible(true);
		String file_name = fd.getFile();
		String dir_name = fd.getDirectory();
		//System.out.println("File location:\n"+dir_name+"\\"+file_name);
		File opened_file = new File(dir_name+"\\"+file_name);
		System.out.println(opened_file.getAbsolutePath());
		System.out.println("File_name = "+file_name);
		if(file_name!=null)
		{
			try
			{
				FileOutputStream fos = new FileOutputStream(opened_file);
				String write = txt.getText();
				int ch;
				for(int i=0;i<write.length();i++)
				{
					ch = (int)write.charAt(i);
					fos.write(ch);
				}
				f.setTitle(file_name);
				
				file_path = opened_file.getAbsolutePath();
				file_content = write;			
			}
			catch(Exception e1)
			{
					System.out.print("Exception at fsaveas \t"+e1.getMessage());
			}
		}
		else
		{
			System.out.println("Cancel is clicked");
			isCancel=true;
		}
	}
	
	public void fsave()
	{
		if(file_path.equals(default_path))
		{
			System.out.println("run fsaveas");
			fsaveas();
		}
		else
		{
			System.out.println("Run save");
			//save code
			try
			{
				FileOutputStream fos = new FileOutputStream(file_path);
				String write = txt.getText();
				int ch;
				for(int i=0;i<write.length();i++)
				{
					ch = (int)write.charAt(i);
					fos.write(ch);
				}
				file_content = write;			
			}
			catch(Exception e1)
			{
					System.out.print("Exception at fsave \t"+e1.getMessage());
					e1.printStackTrace();
			}
		}
	}
	
	public void fnew()
	{
		try
		{
			File file = new File("temp");
			String str;
			if(!file.isDirectory())
				file.mkdir();
			str=file.getAbsolutePath();
			str=str+"\\untitled.txt";
			f.setTitle("untitled.txt");
			txt.setText("");
			FileOutputStream fos = new FileOutputStream(str);			//File named "untitled" is created
			file_path = str;
			default_path = str;
			file_content="";			
		}
		catch(Exception e1)
		{
			System.out.print("Exception in fnew \t"+e1.getMessage());
		}			
	}
	
	public boolean isSaved()			// checks if the currently opened file is saved or not. - returns true if file is saved.
	{
		String currenttext = txt.getText();
		//System.out.println("Current Text = \n"+currenttext + "\nfile_content=\n" + file_content);
		if(currenttext.equals(file_content))
			return true;
		else
			return false;
	}
	
	class SavePrompt extends WindowAdapter implements ActionListener
	{
		Dialog d;
		Button b1,b2,b3;
		Label l;
		Panel p1;
		public SavePrompt()
		{
			d = new Dialog(f,"Want to save?",Dialog.ModalityType.APPLICATION_MODAL);
			d.setSize(200,100);
			b1 = new Button("Yes");
			b2 = new Button("No");
			b3 = new Button("Cancel");
			Label l = new Label("Do you want to save?");
			Panel p1 = new Panel();
			
			p1.add(b1);
			p1.add(b2);
			p1.add(b3);
			
			d.add(l);
			d.add(p1,"South");
			
			d.addWindowListener(this);
			
			
			b1.addActionListener(this);
			b2.addActionListener(this);
			b3.addActionListener(this);
			
		}
		
		public void windowClosing(WindowEvent e)
		{
			d.setVisible(false);
			isCancel = true;
		}
		public void openAskSaveDialog()
		{
			d.setVisible(true);
		}
		
		public void actionPerformed(ActionEvent ae)
		{
			Object o = ae.getSource();
			if(o==b1)
			{
				System.out.println("Yes is clicked");
				isCancel = false;
				fsave();	
				d.setVisible(false);
			}
			if(o==b2)
			{
				System.out.println("No is clicked");
				isCancel = false;
				d.setVisible(false);
			}
			if(o==b3)
			{
				System.out.println("Cancel is clicked");
				isCancel = true;
				d.setVisible(false);
			}
		}
		
		
	}
	
	class Find extends WindowAdapter implements ActionListener,TextListener
	{
		Dialog find;		
		Label find_label;
		TextField find_tf;
		Button find_b1,find_b2;
		Panel find_p1;
		
		String currenttxt;
		int currentposition;
		String currentfind;
		int nc;
		boolean isFind=true;

		public Find()
		{
			find = new Dialog(f,false);
			find.setSize(200,120);
			
			find_label = new Label("Find :");
			find_tf = new TextField();
			find_b1 = new Button("Find Next");
			find_b2 = new Button("Close");
			
			find_b1.setEnabled(false);
			
			find.setTitle("Find :");
			
			find_p1 = new Panel(new FlowLayout());
			find_p1.add(find_b1);
			find_p1.add(find_b2);
			
			find.add(find_label,"North");
			find.add(find_tf,"Center");
			find.add(find_p1,"South");
			
			//find.setAlwaysOnTop(true);
			
			/*Adding actionlistener,windowlisterner to all the button in Find*/
			find_b1.addActionListener(this);
			find_b2.addActionListener(this);
			find.addWindowListener(this);
			
			find_tf.addTextListener(this);
			
		}		
		public void windowClosing(WindowEvent e)
		{
			find.setVisible(false);
			find_tf.setText("");
			firstrun = true;
		}
		
		public void textValueChanged(TextEvent e)
		{
			if(find_tf.getText().length()==0)
			{
				find_b1.setEnabled(false);
			}
			else
				find_b1.setEnabled(true);
		}
		
		public void show()
		{
			find.setVisible(true);
			findreplacebox.hide();
			firstrun = true;
		}
		
		public void actionPerformed(ActionEvent ae)
		{
			Object o = ae.getSource();
			
			if(o==find_b1)
			{
				System.out.println("Find Next is clicked");
				findit();
			}
			if(o==find_b2)
			{
				System.out.println("Close is clicked");
				find.setVisible(false);
				find_tf.setText("");
				firstrun=true;
			}
		}
		
		public void findit()
		{
			String newtxt = txt.getText();
			String newfindtxt = find_tf.getText();
			int newpos = txt.getCaretPosition();
			
			if(firstrun || (newpos!=currentposition) || !(newtxt.equals(currenttxt)) || !(newfindtxt.equals(currentfind)))
			{
				System.out.println("Inside firstrun");
				currenttxt = txt.getText();
				currentposition = txt.getCaretPosition();
				currentfind = find_tf.getText();
				
				p = Pattern.compile(Pattern.quote(find_tf.getText()));
				m = p.matcher(currenttxt);
				
				if(txt.getSelectedText().length()>0)
					currentposition = txt.getSelectionEnd();
				if(m.find(currentposition))
				{
					m.find(currentposition);
					count_new_line(m.start());
					txt.select(m.start()-nc,m.end()-nc);
					isFind = true;
					currentposition = txt.getCaretPosition();
				}
				else
				{
					System.out.println("No match found");
					nomatchfound.show();
					isFind = false;
				}
				firstrun = false;	

			}
			else
			{
				
				if(m.find())
				{
					count_new_line(m.start());
					txt.select(m.start()-nc,m.end()-nc);	
					isFind = true;
				}
				else
				{
					System.out.print("No match found");
					nomatchfound.show();
					isFind = false;
				}
			}
			f.toFront();
			currentposition = txt.getCaretPosition();
		}
		
		public void count_new_line(int n)
		{
			nc=0;
			
			String str = txt.getText();
			int ch;
			for(int i=0;i<=n;i++)
			{
				ch = str.charAt(i);
				if(ch==10)
				{
					nc++;
					//System.out.println(nc);
				}
			}
		}
		
		public void hide()
		{
			find.setVisible(false);
		}
	}
	
	class FindReplace extends Find implements ActionListener,TextListener
	{
		Dialog find_replace;
		Label fr_label1,fr_label2;
		TextField fr_tf1,fr_tf2;
		Panel fr_p1,fr_p2;
		Button fr_b1,fr_b2,fr_b3,fr_b4;
		public FindReplace()
		{
			find_replace = new Dialog(f,false);
			find_replace.setSize(400,200);
			
			fr_label1 = new Label("Find Next");
			fr_label2 = new Label("Replace With");
			
			fr_tf1 = new TextField();
			fr_tf2 = new TextField();
			
			fr_p1 = new Panel(new GridLayout(4,1));
			fr_p2 = new Panel(new FlowLayout());
			
			fr_b1 = new Button("Find Next");
			fr_b2 = new Button("Replace");
			fr_b3 = new Button("Replace All");
			fr_b4 = new Button("Close");
			
			fr_p1.add(fr_label1);
			fr_p1.add(fr_tf1);
			fr_p1.add(fr_label2);
			fr_p1.add(fr_tf2);
			
			fr_p2.add(fr_b1);
			fr_p2.add(fr_b2);
			fr_p2.add(fr_b3);
			fr_p2.add(fr_b4);
			
			find_replace.add(fr_p1,"Center");
			find_replace.add(fr_p2,"South");
			
			fr_b1.setEnabled(false);
			fr_b2.setEnabled(false);
			fr_b3.setEnabled(false);
			
			/*Adding actionlistener to all the button in Find and Replace*/
			
			fr_b1.addActionListener(this);
			fr_b2.addActionListener(this);
			fr_b3.addActionListener(this);
			fr_b4.addActionListener(this);
			
			find_replace.addWindowListener(this);
			
			fr_tf1.addTextListener(this);
			
			/*Find textfield and find dialog now refers to findreplace textfield and findreplace dialog so that findit() runs smoothly in this. */
			find_tf = fr_tf1;
			find = find_replace;
			
		}
		public void show()
		{
			find_replace.setVisible(true);
			findbox.hide();
			firstrun = true;
		}
		
		public void hide()
		{
			find_replace.setVisible(false);
		}
		
		public void actionPerformed(ActionEvent e)
		{
			Object o = e.getSource();
			
			if(o==fr_b1)
			{
				System.out.println("Find Next is clicked");
				findit();
			}
			if(o==fr_b2)
			{
				System.out.println("Replace is clicked");
				fReplace();
			}
			if(o==fr_b3)
			{
				System.out.println("Replace All is clicked");
				fReplaceAll();
			}
			if(o==fr_b4)
			{
				System.out.println("Close is clicked");
				find_replace.setVisible(false);
				fr_tf1.setText("");
				firstrun = true;
			}
		}
		
		public void fReplace()
		{
			if(!firstrun)
			{			
				if(isFind)
				{
					String replacetext = fr_tf2.getText();
					int x = txt.getSelectionStart()-nc;
					int y = txt.getSelectionEnd()-nc;
					txt.replaceRange(replacetext,x,y);
				}
			}
			findit();
		}
		
		public void fReplaceAll()
		{
			findit();
			if(isFind)
			{
				String nice = m.replaceAll(fr_tf2.getText());
				txt.setText(nice);
			}
		}
		
		public void textValueChanged(TextEvent e)
		{
			if(fr_tf1.getText().length()==0)
			{
				fr_b1.setEnabled(false);
				fr_b2.setEnabled(false);
				fr_b3.setEnabled(false);
			}
			else
			{
				fr_b1.setEnabled(true);
				fr_b2.setEnabled(true);
				fr_b3.setEnabled(true);
			}
		}
		public void windowClosing(WindowEvent e)
		{
			find_replace.setVisible(false);
			fr_tf1.setText("");
			firstrun = true;
		}
	}
	
	public void textValueChanged(TextEvent e)
	{
		if(txt.getText().length()>0)
		{
			fnd.setEnabled(true);
			fndrep.setEnabled(true);
		}
		else
		{
			fnd.setEnabled(false);
			fndrep.setEnabled(false);
		}
	}
	
	public static void main(String args[])
	{
		Editor edit = new Editor();
	}
	
	class NoMatch extends WindowAdapter implements ActionListener
	{
		Dialog d;
		Label l;
		Button b;
		
		public NoMatch()
		{
			d = new Dialog(f,true);
			d.setSize(100,100);
			l = new Label("No Match Found");
			b = new Button("OK");
			
			d.add(l);
			d.add(b,"South");
			
			b.addActionListener(this);
			d.addWindowListener(this);
			d.setLocation(new Point(200,200));
		}
		public void show()
		{
			d.setVisible(true);
		}
		
		public void actionPerformed(ActionEvent e)
		{
			Object o = e.getSource();
			if(o==b)
			{
				d.setVisible(false);
			}
		}
		public void windowClosing(WindowEvent e)
		{
			d.setVisible(false);
		}
		
	}
}