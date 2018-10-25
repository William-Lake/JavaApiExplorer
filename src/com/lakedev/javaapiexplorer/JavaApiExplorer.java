package com.lakedev.javaapiexplorer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

public class JavaApiExplorer
{
	private static final class Wrapper
	{
		static final JavaApiExplorer INSTANCE = new JavaApiExplorer();
	}
	
	private List<JavaClass> javaClasses;
	
	private List<JavaClass> selectedClasses;
	
	private JavaApiExplorer()
	{
		javaClasses = new ArrayList<>();
		
		selectedClasses = new ArrayList<>();
	}
	
	public JavaClass pickRandomClass()
	{
		if (javaClasses.size() == 0)
		{
			javaClasses.addAll(selectedClasses);
			
			selectedClasses.clear();
		}
		
		JavaClass selection = javaClasses.get(ThreadLocalRandom.current().nextInt(javaClasses.size()));
		
		javaClasses.remove(selection);
		
		selectedClasses.add(selection);
		
		return selection;
		
	}
	
	public void loadData()
	{
		CountDownLatch countDownLatch = new CountDownLatch(2);
		
		DataLoader javaDataLoader = buildAndStartDataLoader(countDownLatch, Paths.get("resources/JavaClassData.txt"));
		
		DataLoader javaFxDataLoader = buildAndStartDataLoader(countDownLatch, Paths.get("resources/JavaFxClassData.txt"));
		
		try
		{
			countDownLatch.await();
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		loadClasses(javaDataLoader);
		
		loadClasses(javaFxDataLoader);
	}
	
	private DataLoader buildAndStartDataLoader(CountDownLatch countDownLatch, Path filePath)
	{
		DataLoader dataLoader = new DataLoader(countDownLatch, filePath);
		
		new Thread(dataLoader).start();
		
		return dataLoader;
	}

	private void loadClasses(DataLoader dataLoader)
	{
		List<JavaClass> loadedData = dataLoader.getLoadedData();
		
		javaClasses.addAll(loadedData);
	}
	
	public List<JavaClass> getJavaClasses()
	{
		return javaClasses;
	}

	public static JavaApiExplorer getInstance()
	{
		return Wrapper.INSTANCE;
	}
	
	private class DataLoader implements Runnable
	{
		
		private CountDownLatch countDownLatch;
		
		private Path filePath;
		
		private List<JavaClass> loadedData;
		
		public DataLoader(CountDownLatch countDownLatch, Path filePath)
		{
			this.countDownLatch = countDownLatch;
			
			this.filePath = filePath;
			
			loadedData = new ArrayList<>();
		}

		@Override
		public void run()
		{
			try
			{
				List<String> data = Files.readAllLines(filePath, StandardCharsets.ISO_8859_1);
				
				data
				.stream()
				.forEach(item -> 
				{
					String[] items = item.trim().split("~");
					
					try
					{
						String className = items[0];
						
						String packageName = items[1];
						
						String urlSuffix = items[2];
						
						String classDescription = items[3];
						
						String javaDocUrl = 
								urlSuffix.contains("javafx") ?
										"https://docs.oracle.com/javase/8/javafx/api/" + urlSuffix :
											"https://docs.oracle.com/javase/8/docs/api/" + urlSuffix;
											
						String programCreekUrl = 
								"https://www.programcreek.com/java-api-examples/index.php?api=" + 
										(packageName.contains("<") ?
												packageName.replaceAll("<.+?>", "") :
													packageName);
						
						loadedData.add(new JavaClass(className, classDescription, javaDocUrl, programCreekUrl));
						
					} catch (ArrayIndexOutOfBoundsException e)
					{
						System.out.println(item);
					}

				});				
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			
			countDownLatch.countDown();
		}
		
		public List<JavaClass> getLoadedData()
		{
			return loadedData;
		}
	}
}
