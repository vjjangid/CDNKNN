package maven.KNN.testing;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

//class to store distance detail where id represent the point number/ feature number
//distance represent the distance of particular point
class DistanceFrame
{
	int id;
	int code1_id;
	int code2_id;
	
	Float distance;
	
	public DistanceFrame(int id, Float distance, int code1_id, int code2_id )
	{
		this.id = id;
		this.distance = distance;
		this.code1_id = code1_id;
		this.code2_id = code2_id;
	}
	
}

//Implementation of comparator to sort distance
class sortByDistance implements Comparator<DistanceFrame>
{

	public int compare(DistanceFrame o1, DistanceFrame o2) {
		DistanceFrame s1 = (DistanceFrame)o1;
		DistanceFrame s2 = (DistanceFrame)o2;
		if(s1.distance == s2.distance)
			return 0;
		else if(s1.distance > s2.distance)
			return 1;
		else 
			return -1;	
	}
	
}

class Neighbour
{
	int code1_id;
	int code2_id;
	int neighbours_id;
	
	public Neighbour(int code1_id, int code2_id, int neighbour_id)
	{
		this.code1_id = code1_id;
		this.code2_id = code2_id;
		this.neighbours_id = neighbour_id;
	}
}


public class KNN {
	
	
	static List<String[]> Test_Data = new ArrayList<String[]>();
	static List<Integer[]> Train_Data = new ArrayList<Integer[]>();
	
	public static float euclid_dist(Float v1[], Float v2[])
	{
	 	float distance = 0;
        for(int i=0; i< v1.length; i++)
        {
            float temp = v1[i] - v2[i];
            distance += Math.pow(temp,2);
        }
        distance = (float)(Math.sqrt(distance));
        return (distance);
	}

	public static void predict(int num_Of_Neighbour) throws IOException
	{
		String OUTPUT_FILE = "outputfile1.csv";
		
		File file = new File(OUTPUT_FILE);
		
		FileWriter fileWriter = new FileWriter(file);
		
		CSVWriter csvWriter = new CSVWriter(fileWriter);
		
		String header[] = {"code 1", "code 1 Class", "code 2", "code 2 Class",  "Label"};
		csvWriter.writeNext(header);
		
		System.out.println("hello");
		//List<Integer[]> neighbours = new ArrayList<Integer[]>();
		//List<Neighbour[]> neighbours = new ArrayList<Neighbour[]>();
		
		//Traversing all points in data list where one row in data list represent one
		//class features
		
		for(int i=0; i<Test_Data.size(); i++)
		{
			System.out.println("On Test Data number :: "+i);
			
			
			Integer dataI[] = new Integer[41];
			String temp[] = Test_Data.get(i);
			for(int c=3;c<temp.length;c++)
			{
				dataI[c-3] = Integer.parseInt(temp[c]);
			}
			
			for(int g=0;g<dataI.length;g++)
			{
				System.out.print(dataI[g]+" ");
			}
			System.out.println();
			System.out.println("temp length :: "+ temp.length);
			
			//Integer neighbour_details[] = new Integer[num_Of_Neighbour];
			
		
			//Second loop for comparing one data point to all other data points 
			// in test data set
			for(int j=i+1; j<Test_Data.size(); j++)
			{
				
				DistanceFrame neighbour_details[] = new DistanceFrame[num_Of_Neighbour];
				//leave comparison of one data point with itself
				
				Integer dataJ[] = new Integer[41];
				String tempJ[] = Test_Data.get(j);
				for(int c=3;c<tempJ.length;c++)
				{
					dataJ[c-3] = Integer.parseInt(tempJ[c]);
				}
				
				for(int v = 0;v<dataJ.length;v++)
				{
					System.out.print(dataJ[v] + " ");
				}
				System.out.println();
				System.out.println("length dataj :: " +tempJ.length);
				//merging both data1 and data2 features in one vector
				Float comboData[] = new  Float[82]; //83 classification
				
				int k=0;
				for(; k<41; k++)
				{
					comboData[k] = (dataI[k]).floatValue();
				}
				for(; k<82; k++)
				{
					comboData[k] = dataJ[k-41].floatValue();
				}
				
				for(k=0;k<comboData.length;k++)
				{
					System.out.print(comboData[k]+" ");
				}
				System.out.println();
				System.out.println(comboData.length);
				
				// Just to check whether everything is working fine
				/*for(int b=0; b<comboData.length; b++)
				{
					System.out.print(comboData[b]+" ");
				}
				System.out.println();
				System.out.println("comboData length is :: " +comboData.length);
				*/
				
				//Finding neighbours in training data set
				
				List<DistanceFrame> distances = new ArrayList<DistanceFrame>();
				
				
				
				for(int l=0; l<Train_Data.size(); l++)
				{
					Integer tempL[] = Train_Data.get(l);
					System.out.println("Train Data length :: "+tempL.length);
					Float tData[] = new Float[tempL.length-1];
					
					for(int m=0; m<tempL.length-1; m++)
					{
						tData[m] = (tempL[m]).floatValue();
					}
					
					float dist = euclid_dist(comboData, tData);
					DistanceFrame d = new DistanceFrame(l, dist,i, j);
					distances.add(d);
					
				}
				
				
				Collections.sort(distances, new sortByDistance());
				
				
				/*System.out.println("Sorted");
				for(int b=0;b<distances.size();b++)
				{
					System.out.println(distances.get(b).id + " " + distances.get(b).distance);
				}
				break;*/
				
				
				for(int neig = 0; neig<num_Of_Neighbour; neig++)
				{
					DistanceFrame frame = new DistanceFrame(distances.get(neig).id, distances.get(neig).distance, distances.get(neig).code1_id	, distances.get(neig).code2_id);
					neighbour_details[neig] =  frame;
				}
				
				
				
				try 
				{
					
					
					int count=0;
					
					int output_Label = 0;
					
					for(int p=0; p<neighbour_details.length; p++)
					{
						DistanceFrame tempP = neighbour_details[j];
						
						int label;
						
						int id = tempP.id;
						
						label = Train_Data.get(id)[82];
							
						if(label==1 )
								count++;
						
					}
					
					int majority = (k/2) + 1;
					if(count>=majority)
						output_Label = 1;
					
					if(output_Label == 1)
					{
						String record[] = new String[5];
						record[0] = Test_Data.get(i)[0]; // i'th file address
						record[1] = Test_Data.get(i)[1]; // i'th file class name
						record[2] = Test_Data.get(j)[0]; // j'th file address
						record[3] = Test_Data.get(j)[1]; // j'th class name
						record[4] = Integer.toString(output_Label);
						csvWriter.writeNext(record);
					}
					
					csvWriter.flush();
					csvWriter.close();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				break;
			}
			
			//Till here got details of k neighbours
			
			
			/*Neighbour arr[] = new Neighbour[num_Of_Neighbour];
			for(int m=0; m<num_Of_Neighbour; m++)
			{
				Neighbour tempL = new Neighbour(neighbour_details[m].code1_id, neighbour_details[m].code2_id, neighbour_details[m].id);
				arr[m] = tempL;
			}
			
			neighbours.add(arr);*/
			break;
		}
		
	}
	
	public static void classify(List<Neighbour[]> neighbours, int k)
	{
		
		String OUTPUT_FILE = "outputfile1.csv";
		
		try 
		{
			File file = new File(OUTPUT_FILE);
			
			FileWriter fileWriter = new FileWriter(file);
			
			CSVWriter csvWriter = new CSVWriter(fileWriter);
			
			String header[] = {"code 1", " code 2", "Label"};
			csvWriter.writeNext(header);
			
			for(int i=0; i<neighbours.size(); i++)
			{
				Neighbour temp[] = neighbours.get(i);
				int count=0;
				int output_Label = 0;
				
				for(int j=0;j<temp.length;j++)
				{
					int label = Train_Data.get( temp[j].neighbours_id )[82];
					if( label==1 )
						count++;
				}
				
				int majority = (k/2) + 1;
				if(count>=majority)
					output_Label = 1;
				
				
				
				if(output_Label == 1)
				{
					String record[] = new String[3];
					record[0] = Integer.toString(temp[0].code1_id);
					record[1] = Integer.toString(temp[0].code2_id);
					record[2] = Integer.toString(output_Label);
					csvWriter.writeNext(record);
				}
				
			}
			csvWriter.flush();
			csvWriter.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]) throws Exception
	{
		final long START_TIME = System.nanoTime();
		
		//Reading data for testing data set
		//List<Integer[]> data = new ArrayList<Integer[]>();
		String READ_FILE_PATH = "TestingDataset.csv";
		int j=0;
		
		try 
		{
			File  read_File = new File(READ_FILE_PATH);
			FileReader fileReader = new FileReader(read_File);
			
			CSVReader csvReader = new CSVReader(fileReader);
			
			String record[];
			
			while( (record = csvReader.readNext()) != null )
			{
				Test_Data.add(record);
				j++;
			}
			
			csvReader.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		/*System.out.println(" J :: "+j);
		j=0;
		for(int i=0;i<Test_Data.size();i++)
		{
			String[] temp = new String[Test_Data.get(i).length];
			temp = Test_Data.get(i);
			for(int k=0;k<temp.length;k++)
				System.out.print(temp[k]+ " ");
			System.out.println();
			j++;
		}
		System.out.println(" J :: "+j);
		*/
		
		//Reading data for Training data set
		String READ_TRAIN_FILE = "Type1Clone.csv";
		j=0;
		try 
		{
			File readFile = new File(READ_TRAIN_FILE);
			FileReader fileReader = new FileReader(readFile);
			
			CSVReader csvReader = new CSVReader(fileReader);
			
			String record[];
			
			while( (record = csvReader.readNext()) != null )
			{
				Integer temp[] = new Integer[record.length];
				for(int i=0; i<record.length; i++)
				{
					temp[i] = Integer.parseInt(record[i]);
				}
				Train_Data.add(temp);
				j++;
			}
			csvReader.close();
		}
		catch( Exception e)
		{
			e.printStackTrace();
		}
		
		/*for(int i=0;i<Train_Data.size();i++)
		{
			Integer[] temp = Train_Data.get(i);
			for(int k=0; k<temp.length; k++)
			{
				System.out.print(temp[k]+" ");
			}
			System.out.println();
		}
		System.out.println(" J :: "+j);
		System.out.println(Train_Data.get(0).length);
		*/
		
		predict(3);
		
		/*for(int i=0; i<neighbour.size();i++)
		{
			System.out.println("Test Data Details related to number :: "+i);
			Neighbour temp[] = neighbour.get(i);
			for(int l=0;l<temp.length;l++)
			{
				System.out.println(temp[l].code1_id+" "+temp[l].code2_id+" "+temp[l].neighbours_id);
			}
		}
		
		classify(neighbour,3);
		
		final long END_TIME = System.nanoTime();
        long total = END_TIME - START_TIME;
        System.out.println("\n\t\t\t\t\t\tTime in nano seconds:: "+total);
        long mS = total/1000000;
        System.out.println("\n\t\t\t\t\t\tTime in milli seconds:: "+mS);



        long seconds = mS/1000;
        System.out.println("\n\t\t\t\t\t\tTime in seconds:: "+seconds);
        
        long hours = seconds/3600;
        System.out.println("\n\t\t\t\t\t\tTime in hours :: "+ hours);*/
		
	}
}
