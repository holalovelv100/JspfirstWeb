package com.spring.basic.commons.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.spring.basic.commons.util.FileUtils;

@RestController
public class UploadTestController {
	
//	@Autowired
//	@Qualifier("uploadPath")
	@Resource(name="uploadPath") //上と同じ意味
	private String uploadPath;

	@GetMapping("/uploadForm")
	public ModelAndView uploadForm() {
		return new ModelAndView("test/upload-form");
	}
	
	//クライエントが伝送したファイルを持ってるオブジェクト。MultipartFileをパラメータで指定。
	@PostMapping("/upload")
	public ModelAndView upload(MultipartFile file) throws Exception {
		
		System.out.println("original-name: " + file.getOriginalFilename());
		System.out.println("size: " + file.getSize());
		System.out.println("content-type: " + file.getContentType());
		
		ModelAndView mv = new ModelAndView();
		
		//実際にサーバーのディレクターにアップロードする機能
		String fileName = FileUtils.uploadFile(file, uploadPath);
		mv.addObject("fileName", fileName);
		
		mv.setViewName("test/upload-form");
		return mv;
	}
	
	//drag&dropで伝送されたファイルの格納要請を処理
	@PostMapping(value = "/uploadAjaxes")
	public ResponseEntity<String[]> uploadAjaxes(MultipartFile[] files) throws Exception {
//		System.out.println("original-name: " + file.getOriginalFilename());
//		System.out.println("size: " + file.getSize());
//		System.out.println("content-type: " + file.getContentType());
		
		int len = (files == null) ? 0 : files.length;
		
		try {
			String[] uploadedFileNames = new String[len];
			for (int i = 0; i < len; i++) {
				uploadedFileNames[i] = FileUtils.uploadFile(files[i], uploadPath);				
			}
			return new ResponseEntity<>(uploadedFileNames, HttpStatus.CREATED);
			
		} catch (Exception e) {
			return new ResponseEntity<>(new String[] {e.getMessage()}, HttpStatus.BAD_REQUEST);
		}
	}
	
	//ファイルloadの要請を処理
	// 要請 URI: /loadFile?fileName=~~~~
	@GetMapping("/loadFile")
	public ResponseEntity<byte[]> loadFile(String fileName) throws Exception {	//get要請する時、必ずbyteで割って配列で送る
		
		System.out.println("要請fileName: " + fileName);
		System.out.println("要請ファイルのロケーション: " + uploadPath + fileName);
		
		// 클라이언트가 요청한 파일의 전체경로의 객체
		File file = new File(uploadPath + fileName);
		if(!file.exists())
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);	//オブジェクトがないとNOT_FOUND 

		
		
		//サーバーでfileNameを通じてファイルをローディング。
		//ファイルのローディングの時、InputStreamオブジェクトを使用。
		try(InputStream in = new FileInputStream(file)) {
			//ファイルのローディングが成功したら、ファイルを見つけてローディングした後クライエントへ伝送。
			String ext = FileUtils.getFileExtension(fileName);
			MediaType mType = FileUtils.getMediaType(ext);
			
			
			//ファイルのMIME(Multipurpose Internet Mail Extensions) TypeをHttpHeaderに入れてクライエントへ伝送
			HttpHeaders headers = new HttpHeaders();
			
			//イメージかどうかの可否確認
			if(mType != null) {	  //イメージの場合:単純なサムネイルを読んで送る
				headers.setContentType(mType);
			} else {	//イメージじゃないと添付ファイルのダウンロード機能ができる
				
				//UUIDで生成したファイル名を元に戻す
				fileName = fileName.substring(fileName.lastIndexOf("_") + 1);
				
				//ダウンロード機能を追加
				headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
				
				//ファイル名によってencoding再設定
				String encodingName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
				
				//添付ファイルのタイプでダウンロード
				headers.add("Content-Disposition", "attachment; filename=\"" + encodingName + "\""); 
				//このままfileNameでダウンロードしたら、変なファイル名になるから、上の「UUID」の作業をする。
				//なお、encodingのためfileNameはencodingNameで再設定された変数をもらう
			}
			//一つ目のパラメータ値で…InputStreamで読んだファイルのデータをbyteの配列でかえてクライエントへ伝送
			//二つ目パラメータ値で…ヘッダー情報(headers)を伝送
			return new ResponseEntity<>(IOUtils.toByteArray(in), headers, HttpStatus.OK);
			
		} catch (Exception e) {
			//ファイルローディングのエラーの時クライエントへ伝送するデータを処理
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} //finallyとしてin.close()の代わりにtry()に入れた
		
	}
	
	//サーバーにあるファイルについて除去要請の処理
	// URI: /deleteFile?fileName=/2019/11/17/s_artwrtqawer_abc.jpg
	@DeleteMapping("/deleteFile")
	public ResponseEntity<String> deleteFile(String fileName) throws Exception {
		
		try {
			
			//ファイルの除去
			File delFile = new File(uploadPath + fileName);
			delFile.delete();
			
			//イメージファイルなら元のイメージまで消さなければならない!!
			boolean isImage = FileUtils.getMediaType(FileUtils.getFileExtension(fileName)) != null;
			if(isImage) {	//イメージなら…
				// fileName => サムネイルのイメージ、ロケーション ex) /2019/11/17/s_artwrtqawer_monkey.gif
				// originalName => 元のイメージ、ロケーション ex) /2019/11/17/artwrtqawer_monkey.gif
				int lastSlash = fileName.lastIndexOf("/") +1;
				String originalName = fileName.substring(0, lastSlash) + fileName.substring(lastSlash +2);
				File originalImageFile = new File(uploadPath + originalName);
				originalImageFile.delete();
			}
			
			return new ResponseEntity<>("fileDelSucces", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
	
	}// end deleteFile
	
	
}// end class




