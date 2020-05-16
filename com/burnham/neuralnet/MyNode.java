package com.burnham.neuralnet;

import java.util.Date;
import java.util.Random;

class MyNode
{
	int x = -1;
	int y = -1;
	double value = -1;
	double[] weight = new double[20];
	double lastfitness;
	int label = -1;

	MyNode(int x, int y)
	{
		this.x = x;
		this.y = y;
		Random r = new Random(new Date().getTime());
		for(int i = 0; i < weight.length; i++)
			weight[i] = r.nextDouble();
	}

	MyNode(double thevalue)
	{
		value = thevalue;
	}
}