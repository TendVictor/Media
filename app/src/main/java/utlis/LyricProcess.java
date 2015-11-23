package utlis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import bean.LrcContent;

/**
 * Created by chen on 15/11/23.
 */
public class LyricProcess {
    private List<LrcContent> lrcList; //List集合存放歌词内容对象
    private LrcContent mLrcContent;     //声明一个歌词内容对象

    public LyricProcess() {
        mLrcContent = new LrcContent();
        lrcList = new ArrayList<LrcContent>();
    }

    public String readLRC(String path){
        StringBuilder stringbuilder = new StringBuilder();
        int lastIndex = path.indexOf("Music/") + 5;
        int interuptIndex = path.indexOf("-");

        StringBuilder p =  new StringBuilder(path.substring(0,lastIndex));
        p.append("/");
        p.append(path.substring(interuptIndex+1).trim());


        File f = new File(p.toString().replace(".mp3",".lrc"));
        System.out.println("file Path :" + p.toString());

        try{

            //创建一个文件输入流对象
            FileInputStream fis = new FileInputStream(f);
            InputStreamReader isr = new InputStreamReader(fis, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String s = "";
            while((s = br.readLine()) != null) {
                //替换字符
                s = s.replace("[", "");
                s = s.replace("]", "@");

                //分离“@”字符
                String splitLrcData[] = s.split("@");
                if (splitLrcData.length > 1) {
                    mLrcContent.setLrcStr(splitLrcData[1]);

                    //处理歌词取得歌曲的时间
                    int lrcTime = time2Str(splitLrcData[0]);

                    mLrcContent.setLrcTime(lrcTime);

                    //添加进列表数组
                    lrcList.add(mLrcContent);

                    //新创建歌词内容对象
                    mLrcContent = new LrcContent();
                }
            }

        }catch(FileNotFoundException e){
            e.printStackTrace();
            stringbuilder.append("木有歌词文件，赶紧去下载...");
        }catch(IOException e){
            e.printStackTrace();
            stringbuilder.append("木有读取到歌词...");
        }

        return stringbuilder.toString();
    }

    public int time2Str(String timeStr) {
        timeStr = timeStr.replace(":", ".");
        timeStr = timeStr.replace(".", "@");

        String timeData[] = timeStr.split("@"); //将时间分隔成字符串数组

        //分离出分、秒并转换为整型
        int minute = Integer.parseInt(timeData[0]);
        int second = Integer.parseInt(timeData[1]);
        int millisecond = Integer.parseInt(timeData[2]);

        //计算上一行与下一行的时间转换为毫秒数
        int currentTime = (minute * 60 + second) * 1000 + millisecond * 10;
        return currentTime;
    }
    public List<LrcContent> getLrcList() {
        return lrcList;
    }


}
