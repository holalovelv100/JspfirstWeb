package com.spring.basic.commons.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.springframework.http.MediaType;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

public class FileUtils {
	
	//6. イメージの区別のためなイメージの拡張子マップを作る
	private static Map<String, MediaType> mediaMap;
	static {
		mediaMap = new HashMap<>();
		mediaMap.put("JPG", MediaType.IMAGE_JPEG);
		mediaMap.put("PNG", MediaType.IMAGE_PNG);
		mediaMap.put("GIF", MediaType.IMAGE_GIF);
	}
	//MediaTypeを持ってくるメソッド
	public static MediaType getMediaType(String ext) {
		return mediaMap.get(ext.toUpperCase());	//小文字でしてもuppercaseのため、大文字で
	}

	//1．ファイルのアップロード後、保存ファイル名をreturnするメソッド
	public static String uploadFile(MultipartFile file, String uploadPath) throws IOException {
		
		//重複がないとファイル名で変更する
		// ex) abc.gif -> 3eddf23-dfsd3-dfsdf3-342_abc.gif
		String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
		System.out.println("生成されたファイル名: " + fileName);
		
		// uploadPath: ~~/workspace/~~~/upload/ + ファイル名
		// uploadPath: ~~/workspace/~~~/upload/2019/11/16/ + ファイル名
		String newUploadPath = getNewUploadPath(uploadPath);
		
		//アップロードするファイルをオブジェクトで(コンストラクタへディレクトリとファイル名を伝達)
		File target = new File(newUploadPath, fileName);
		//アップロードしたファイルをコピーしてターゲットのロケーションに保存
		FileCopyUtils.copy(file.getBytes(), target);
		
		//もしアップロードしたファイルがイメージなら、サムネイルを作る
		//イメージじゃないと該当ファイルのアイコンを作る
		
		//拡張子の情報を得る
		String ext = getFileExtension(fileName);
		
		String uploadedFileName = null;
		if(getMediaType(ext) != null) {  
			uploadedFileName = makeThumbnail(uploadPath, newUploadPath, fileName);
		} else {  //イメージじゃないと~
			uploadedFileName = makeIcon(uploadPath, newUploadPath, fileName);
		}
		return uploadedFileName;
	}

	//8.　サムネイルのイメージを生成するメソッド
	private static String makeThumbnail(String plainPath, String uploadDirPath, String fileName) throws IOException {
		
		//元のイメージ読んでくる
		BufferedImage srcImg = ImageIO.read(new File(uploadDirPath, fileName));	// read(input)にはファイルのオブジェクトが入る。
		
		//#　サムネイルの作業
		//1. 元のイメージ、リサイジング
		BufferedImage destImg = Scalr.resize(srcImg, Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_TO_HEIGHT, 100);
		
		//2. サムネイルイメージのロケーション + 名前生成
		String thumbnailName = uploadDirPath + File.separator + "s_" + fileName; //サムネイルは前に「s_」を付ける
		
		//3.　サムネイルのイメージファイルのオブジェクト生成
		File newFile = new File(thumbnailName);
		
		//4. サムネイルのイメージ生成後、サーバーに保存
		ImageIO.write(destImg, getFileExtension(fileName), newFile);
		
		//サムネイルのイメージファイル名だけreturnする(~~/upload/2019/11/16/を別にして名だけ)
		// ~~~/upload/2019/11/16/s_aerawer_abc.jpg
		return thumbnailName.substring(plainPath.length()).replace(File.separatorChar, '/');
	}
	//////////////////////// crop　仕方  ///////////////////////////////////
	public static String makeCrop(String uploadRootPath, String dirname, String filename) throws IOException {
	      BufferedImage srcImg = ImageIO.read(new File(dirname, filename));
	      
	      int w = srcImg.getWidth();  // 600
	      int h = srcImg.getHeight(); // 400
	      int min = Math.min(w, h);   // 400
	      
	      //(100, 0)から横400、縦400ほどcrop
	      //                                            100,          0,     ~400, ~400
	      BufferedImage tmpImg = Scalr.crop(srcImg, (w - min)/2, (h - min)/2, min, min);
	      
	      BufferedImage destImg = Scalr.resize(tmpImg, Scalr.Method.AUTOMATIC,
	            Scalr.Mode.FIT_TO_HEIGHT, 300);
	      
	      String ext = getFileExtension(filename);
	      String cropName = dirname + File.separator + "c_" + filename;
	      File newFile = new File(cropName);
	      ImageIO.write(destImg, ext.toUpperCase(), newFile);
	      
	      return cropName.substring(uploadRootPath.length()).replace(File.separatorChar, '/');
	   }

	
	
	//9. アイコンのロケーションをreturnするメソッド
	private static String makeIcon(String plainPath, String uploadDirPath, String fileName) {
		
		String iconName = uploadDirPath + File.separator + fileName;
		return iconName.substring(plainPath.length()).replace(File.separatorChar, '/');
		
	}

	//ファイル名から拡張子を抽出するメソッド
	public static String getFileExtension(String fileName) {
		// ex) 324asdewejlkflsjew_山.gif
		return fileName.substring(fileName.lastIndexOf(".") + 1);//ファイル名の拡張子の前のある「。」を見つける
	}

	//2.　日別にアップロードをするフォルダ生成後、新しいロケーションをreturnするメソッド
	//ex) uploadPath + /2019/11/16
	private static String getNewUploadPath(String uploadPath) {
		
		Calendar cal = Calendar.getInstance();
		int y = cal.get(Calendar.YEAR);
		int m = cal.get(Calendar.MONTH) + 1;
		int d = cal.get(Calendar.DATE);
		
		return makeDir(uploadPath, String.valueOf(y), len2(m), len2(d)); //逆 slashを使ったら脱出文字で認識
									// String.valueOf(y) または ""+y でも可能。

	}
	
	//4. アップロードの日別にフォルダを生成されるメソッド
	// uploadPath: ~~/workspace/~~~/upload/
	//もし該当フォルダが存在しないと．．．
	// a) uploadフォルダの下に2019フォルダを生成  ~~~/upload/2019
	// b) 2019フォルダの下に 11ォルダを生成  ~~~/upload/2019/11
	// c) 10フォルダの下に  16ォルダを生成  ~~~/upload/2019/11/16
	private static String makeDir(String uploadPath, String... dates) {
		
		for (String dateInfo : dates) {
			// File.separator : windowsの場合 \\, linuxの場合「/」を表示する
			uploadPath += File.separator + dateInfo; //WindowやLinuxでのスラッシュの方向が違うから統合するためなseparator。
			File dirName = new File(uploadPath);
			if(!dirName.exists()) { //該当ディレクトリ名が存在しないと．．．
				dirName.mkdir(); //フォルダを作れ~~  
			} else {
				continue;
			}
		}
		return uploadPath; //日付情報が含まれたuploadPath。
	}
	
	//3.　月、日をつねに二桁で表現するメソッド
	private static String len2(int n) {
		//数字を文字列でformattingするオブジェクト、DecimalFormat。
		return new DecimalFormat("00").format(n).toString();
	}
	
	
	public static void main(String[] args) {
		//System.out.println(getNewUploadPath("\\upload"));
		System.out.println(getFileExtension("4509asd834qweartawtrqwre_山.png"));
	}
	
}







