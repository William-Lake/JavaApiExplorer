package com.lakedev.javaapiexplorer;

public class JavaClass implements Comparable<JavaClass>
{
	private String className;
	
	private String classDescription;
	
	private String javaDocUrl;
	
	private String programCreekUrl;

	public JavaClass(String className, String classDescription, String javaDocUrl, String programCreekUrl)
	{
		super();
		this.className = className;
		this.classDescription = classDescription;
		this.javaDocUrl = javaDocUrl;
		this.programCreekUrl = programCreekUrl;
	}

	public String getClassName()
	{
		return className;
	}

	public String getClassDescription()
	{
		return classDescription;
	}

	public String getJavaDocUrl()
	{
		return javaDocUrl;
	}

	public String getProgramCreekUrl()
	{
		return programCreekUrl;
	}
	
	@Override
	public String toString()
	{
		return className;
	}

	@Override
	public int compareTo(JavaClass other)
	{
		return className.compareTo(other.getClassName());
	}
	
}
