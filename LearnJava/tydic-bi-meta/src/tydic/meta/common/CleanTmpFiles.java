package tydic.meta.common;


import java.io.File;
import java.io.IOException;

import tydic.frame.common.Log;
import tydic.meta.module.mag.timer.IMetaTimer;

public class CleanTmpFiles implements IMetaTimer{

	@Override
	public void init() {
		
	}

	@Override
	public void run(String timerName) {
		
		String dir = null;
		try {
			
			dir = new File(new File("").getCanonicalPath()).getParent()+"/webapps/tydic-bi-meta/upload/template/";//获取当前项目所在路径
			System.out.println(dir);
			File[] files=new File(dir).listFiles();					//列出临时目录下所有的文件
			
			if(files!=null&&files.length>0){			//删除临时文件夹下的所有文件
				for(File file:files){
					if(file.exists())
						file.delete();
				}
			}
		
		} catch (IOException e) {
			Log.error("删除文件发生异常",e);
		}
	}
}
