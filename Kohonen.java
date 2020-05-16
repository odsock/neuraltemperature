/*	Mike Burnham
	CS405 - AI
	hw5 exersize 2b
	12/14/2006

	RESULTS:
			This program seems to do fairly well.  When the number of loops through
		the training data is increased (10000) it seems to do much better.
		The learning factor may be why.  I was using .5 to start with, but .2 seemed
		to get it learning faster.
			It still often mistakes 0 for 8, and sometimes doesn't end up labeling
		a node at all for some classes.  I think that is due to them getting replaced
		repeatedly durning training.
*/

import java.util.*;
import java.io.*;
import java.lang.*;

public class Kohonen
{
	MyNode[] input = new MyNode[20];
	MyNode[][] map = new MyNode[10][10];
	int currenttrain = -1;
	double learningfactor = .2;

	public static void main(String args[])
	{
		Kohonen k = new Kohonen();

		for(int i = 0; i < 1000; i++)
		{
			k.loadNextTrain();
			MyNode bestfit = k.findBestFit();
			System.out.println("train " + k.currenttrain + " bestfit " + bestfit.x + "," + bestfit.y + " fitness " + bestfit.lastfitness);
			bestfit.label = k.currenttrain;
			k.improveFitness(bestfit);
		}

		System.out.println();
		System.out.println("current labels");
		for(int i = 0; i < 10; i++)
		{
			for(int j = 0; j < 10; j++)
				System.out.print(String.format("%3d", k.map[i][j].label));
			System.out.println();
		}
		System.out.println();

		System.out.println("done with training data.");

		while(true)
		{
			System.out.println("\nenter 4x5 bit pattern.");
			String temp = getUserPattern();
			k.setInputValues(temp);
			MyNode bestfit = k.findBestFit();
			System.out.println("bestfit " + bestfit.x + "," + bestfit.y + " label: " + bestfit.label);
		}
	}

	static String getUserPattern()
	{
		String pattern = "";
		try
		{
			InputStreamReader isr = new InputStreamReader(System.in);
			BufferedReader br = new BufferedReader(isr);
			for(int i = 0; i < 5; i++)
				pattern += br.readLine();
		}
		catch(IOException e)
		{
			System.out.println(e);
			System.exit(0);
		}

		return pattern;
	}

	Kohonen()
	{
		for(int i = 0; i < 10; i++)
			for(int j = 0; j < 10; j++)
				map[i][j] = new MyNode(i, j);
	}

	public void setInputValues(String bitpattern)
	{
		for(int i = 0; i < bitpattern.length(); i++)
			input[i] = new MyNode(Double.parseDouble(bitpattern.substring(i, i+1)));
	}

	public void improveFitness(MyNode bestfit)
	{
		int x = bestfit.x;
		int y = bestfit.y;

		MyNode n;

		if(x-2 >= 0)
		{
			n = map[x-2][y];
			for(int i = 0; i < 20; i++)
				n.weight[i] += (input[i].value - n.weight[i]) * learningfactor;
		}
		for(int j = y - 1; j < y + 1; j++)
			if(j >= 0 && j < 10 && x-1 >= 0)
			{
				n = map[x-1][j];
				for(int i = 0; i < 20; i++)
					n.weight[i] += (input[i].value - n.weight[i]) * learningfactor;
			}
		for(int j = y - 2; j < y + 2; j++)
			if(j >= 0 && j < 10)
			{
				n = map[x][j];
				for(int i = 0; i < 20; i++)
					n.weight[i] += (input[i].value - n.weight[i]) * learningfactor;
			}
		for(int j = y - 1; j < y + 1; j++)
			if(j >= 0 && j < 10 && x+1 < 10)
			{
				n = map[x+1][j];
				for(int i = 0; i < 20; i++)
					n.weight[i] += (input[i].value - n.weight[i]) * learningfactor;
			}
		if(x+2 < 10)
		{
			n = map[x+2][y];
			for(int i = 0; i < 20; i++)
				n.weight[i] += (input[i].value - n.weight[i]) * learningfactor;
		}
	}

	public MyNode findBestFit()
	{
		double[][] fitnessmap = new double[10][10];
		MyNode bestfit = null;
		double bestsofar = 100000;

		for(int i = 0; i < 10; i++)
			for(int j = 0; j < 10; j++)
			{
				for(int k = 0; k < 20; k++)
				{
					fitnessmap[i][j] += Math.pow(map[i][j].weight[k] - input[k].value, 2);
					map[i][j].lastfitness = fitnessmap[i][j];
				}
				if(fitnessmap[i][j] < bestsofar)
				{
					bestsofar = fitnessmap[i][j];
					bestfit = map[i][j];
				}
			}

		return bestfit;
	}

	public MyNode fitUserPattern()
	{
		double[][] fitnessmap = new double[10][10];
		MyNode bestfit = null;
		double bestsofar = 100000;

		for(int i = 0; i < 10; i++)
			for(int j = 0; j < 10; j++)
			{
				for(int k = 0; k < 20; k++)
				{
					fitnessmap[i][j] += Math.pow(map[i][j].weight[k] - input[k].value, 2);
					map[i][j].lastfitness = fitnessmap[i][j];
				}
				if(fitnessmap[i][j] < bestsofar)
				{
					bestsofar = fitnessmap[i][j];
					bestfit = map[i][j];
				}
			}

		if(bestfit.label == -1)
		{
			int[] labels = new int[10];
			int i = bestfit.x;
			int j = bestfit.y;

			System.out.println("no label: " + i + "," + j);

			int offset = 1;
			i -= offset;
			j -= offset;
			while(true)
			{
				if(i < 10 && i >= 0 && j < 10 && j >= 0 && map[i][j].label != -1)
					labels[map[i][j].label]++;

				if(i == bestfit.x - 1 - offset && j == bestfit.y - offset)
				{
					int maxlabel = 0;
					int label = -1;
					for(int k = 0; k < labels.length; k++)
						if(labels[k] > maxlabel)
						{
							maxlabel = labels[k];
							label = k;
						}
					if(label == -1)
					{
						offset++;
						if(offset == 10)
							break;
						j++;
					}
					else
					{
						bestfit.label = label;
						break;
					}
				}
				else if(i == bestfit.x - offset && j < bestfit.y + offset)
					j++;
				else if(i == bestfit.x + offset && j > bestfit.y - offset)
					j--;
				else if(j == bestfit.y - offset && i > bestfit.x - offset)
					i--;
				else if(j == bestfit.y + offset && i < bestfit.x + offset)
					i++;

				System.out.println("next check: " + i + "," + j);
			}
		}


		return bestfit;
	}

	//read a training set
	public void loadNextTrain()
	{
		currenttrain = ++currenttrain % 10;
		String train = "";
		try
		{
			File f = new File("train.txt");
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String nextline = br.readLine();
			while(nextline != null)
			{
				if(nextline.equals(String.valueOf(currenttrain)))
				{
					for(int i = 0; i < 5; i++)
						train += br.readLine();
					break;
				}
				nextline = br.readLine();
			}
		}
		catch(IOException e)
		{
			System.out.println(e);
			System.exit(0);
		}

		for(int i = 0; i < train.length(); i++)
			input[i] = new MyNode(Double.parseDouble(train.substring(i, i+1)));
	}
}

class MyNode
{
	int x = -1;
	int y = -1;
	double value = -1;
	double[] weight = new double[20];
	double lastfitness;
	int label = -1;

	MyNode(int thex, int they)
	{
		x = thex;
		y = they;
		Random r = new Random(new Date().getTime());
		for(int i = 0; i < weight.length; i++)
			weight[i] = r.nextDouble();
	}

	MyNode(double thevalue)
	{
		value = thevalue;
	}
}