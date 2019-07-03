package com.bdsoft.crawler.common;


import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件工具类
 * 
 * @author 丁辰叶
 * @date 2016-5-5
 * @version 1.0.0
 */
public class BDFileUtil {

	/** The size of blocking to use */
	private static final int BLKSIZ = 2048;

	// 文件格式与文件魔数映射
	private static final Map<String, String> FILE_TYPE_MAP = new HashMap<String, String>();

	// 图片透明像素值
	private static int TM_RGB = 16777215;
	private static int TM_RGB2 = -16777216;

	static {
		/* 图片文件 */
		FILE_TYPE_MAP.put("ffd8ff", "jpg");
		FILE_TYPE_MAP.put("89504e", "png");
		FILE_TYPE_MAP.put("424d2e", "bmp");// 256色位图(bmp)
		FILE_TYPE_MAP.put("424d1e", "bmp");// 24色位图(bmp)
		FILE_TYPE_MAP.put("424dc6", "bmp");// 16色位图(bmp)
		FILE_TYPE_MAP.put("474946", "gif");
		FILE_TYPE_MAP.put("49492a", "tif");
		FILE_TYPE_MAP.put("384250", "psd");

		/* 视频文件 */
		FILE_TYPE_MAP.put("464c56", "flv");
		FILE_TYPE_MAP.put("000000", "mp4");
		FILE_TYPE_MAP.put("1a45df", "mkv");
		FILE_TYPE_MAP.put("2e524d", "rmvb");
		FILE_TYPE_MAP.put("524946", "avi");
		FILE_TYPE_MAP.put("4d5a50", "exe");

		/* 音频文件 */
		FILE_TYPE_MAP.put("232141", "amr");

		/* 文档文件 */
		FILE_TYPE_MAP.put("255044", "pdf");
		FILE_TYPE_MAP.put("d0cf11", "doc");		
		FILE_TYPE_MAP.put("504b03", "xlsx");
	}

	public static void main(String[] args) {
//		String name = "T10（U欢-小:金|鱼）是吗?的<值>得*奋\"斗";
//		System.out.println(name);
//		System.out.println(safeFileName(name));
//
//		File file = new File("d:/home/公众号类型.png");
//		System.out.println(getFileName(file));
//		System.out.println(file.getParent());

		File[] gcvs = new File("D:/files").listFiles();
		for (File v : gcvs) {
			String ft = getFileType(v);
			System.out.println(String.format("文件：%s  类型：%s", v.getName(), ft));
		}

	}

	/**
	 * 获取文件名
	 * 
	 * @param file
	 * @return
	 */
	public static String getFileName(File file) {
		String name = file.getName();
		return name.substring(0, name.lastIndexOf("."));
	}

	/**
	 * 将原图修成正方形，默认白底
	 * 
	 * @param img
	 *            图片文件
	 */
	public static String setImgZfx(File img) {
		return setImgZfx(img, Color.WHITE);
	}

	public static String setImgZfx(File img, Color def) {
		String type = BDFileUtil.getFileType(img);
		String name = BDFileUtil.getFileName(img) + "-zfx." + type;
		String out = img.getParent() + File.separator + name;
		try {
			BufferedImage src = ImageIO.read(img);
			int w = src.getWidth();
			int h = src.getHeight();
			if (w == h) {
				return "";
			}

			int nw = 0, nh = 0, y = 0, x = 0;
			if (w > h) {
				nw = w;
				nh = w;
				y = (w - h) / 2;
				x = 0;
			} else {
				nw = h;
				nh = h;
				y = 0;
				x = (h - w) / 2;
			}

			BufferedImage dest = new BufferedImage(nw, nh, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = dest.createGraphics();

			Color cl = new Color(src.getRGB(w / 2, 0));
			g.setColor((def != null) ? def : cl);
			g.fillRect(0, 0, nw, nh);
			g.drawImage(src, null, x, y);
			ImageIO.write(dest, "png", new File(out));
			System.out.println("图片加宽为正方形 >> " + out);
			return out;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 将png透明图添加白背景
	 * 
	 * @param png
	 */
	public static String setPngUnTm(File png) {
		if (BDFileUtil.isTmPic(png)) {
			String type = BDFileUtil.getFileType(png);
			String name = BDFileUtil.getFileName(png) + "-untm." + type;
			String out = png.getParent() + File.separator + name;
			try {
				BufferedImage bi = ImageIO.read(png);
				int w = bi.getWidth();
				int h = bi.getHeight();
				BufferedImage nbi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
				Graphics2D g = nbi.createGraphics();
				g.setBackground(Color.white);
				g.fillRect(0, 0, w, h);
				g.drawImage(bi, null, 0, 0);
				ImageIO.write(nbi, "png", new File(out));
				System.out.println("图片添加白背景 >> " + out);
				return out;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	/**
	 * 图片是否透明
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isTmPic(String file) {
		if (StringUtils.isEmpty(file)) {
			throw new RuntimeException("file can't be null");
		}
		return isTmPic(new File(file));
	}

	/**
	 * 图片是否透明
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isTmPic(File file) {
		if (file == null) {
			throw new RuntimeException("file can't be null");
		}
		if (!file.exists()) {
			throw new RuntimeException("文件不存在：" + file.getAbsolutePath());
		}
		if (!"png".equals(getFileType(file))) {
			return false;
		}
		boolean isTm = false;
		// int i = 0, j = 0;
		try {
			BufferedImage bi = ImageIO.read(file);
			int w = bi.getWidth() / 4;
			int h = bi.getHeight() / 4;
			for (int x = 0; x < h; x++) {
				// i = x;
				for (int y = 0; y < w; y++) {
					// j = y;
					int dip = bi.getRGB(x, y);
					// System.out.print(dip + " ");
					if (dip == 0 || dip == TM_RGB || dip == TM_RGB2) {
						isTm = true;
						break;
					}
				}
				// System.out.println();
				if (isTm) {
					break;
				}
			}
		} catch (Exception e) {
			// System.out.println(i + " x " + j);
			e.printStackTrace();
		}
		return isTm;
	}

	/**
	 * 获取文件类型
	 * 
	 * @param file
	 * @return
	 */
	public static String getFileType(String file) {
		if (StringUtils.isEmpty(file)) {
			throw new RuntimeException("file can't be null");
		}
		return getFileType(new File(file));
	}

	/**
	 * 获取文件类型
	 * 
	 * @param file
	 * @return
	 */
	public static String getFileType(File file) {
		if (file == null) {
			throw new RuntimeException("file can't be null");
		}
		if (!file.exists()) {
			throw new RuntimeException("文件不存在：" + file.getAbsolutePath());
		}
		try {
			return getFileType(new FileInputStream(file));
		} catch (Exception e) {
			throw new RuntimeException("文件类型获取失败：" + e.getMessage());
		}
	}

	/**
	 * 获取文件类型
	 * 
	 * @param in
	 * @return
	 */
	public static String getFileType(InputStream in) {
		BufferedInputStream bis = null;
		try {
			bis = new BufferedInputStream(in);
			byte[] type = new byte[3];
			bis.mark(type.length);
			bis.read(type);
			String tmp = Utils.bytesToHexString(type);
			System.out.println(String.format("文件头：%s", tmp));
			return FILE_TYPE_MAP.get(tmp);
		} catch (Exception e) {
			throw new RuntimeException("文件类型获取失败：" + e.getMessage());
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
	}

	/**
	 * 将非法文件名全部替换成-
	 * 
	 * @param name
	 * @return
	 */
	public static String safeFileName(String name) {
		return name.replaceAll("[\\|/|\\||:|\\*|\\?|<|>|\"]", "-");
	}

	/**
	 * 类路径
	 */
	public static String getClassPath() {
		URL url = BDFileUtil.class.getClassLoader().getResource("");
		if (null == url) {
			return "";
		}
		return url.getPath();
	}

	/**
	 * 取到目录为webRoot
	 */
	public static String getWebRoot() {
		String webRoot = Thread.currentThread().getContextClassLoader().getResource("/").getPath();
		if ("Win".equalsIgnoreCase(System.getProperty("os.name").substring(0, 3))) {
		}
		System.out.println("webroot=" + webRoot);
		return webRoot;
	}

	/**
	 * 覆盖写入
	 * 
	 * @param path
	 *            路径
	 * @param content
	 *            内容
	 */
	public static void writeFile(String path, String content) {
		writeFile(path, content, true);
	}

	/**
	 * 将文本内容写入文件
	 * 
	 * @param path
	 *            全路径：/dir/file.type
	 * @param content
	 *            文本内容
	 * @param flag
	 *            是否覆盖
	 */
	public static void writeFile(String path, String content, boolean flag) {
		BufferedWriter out = null;
		try {
			File f = new File(path);
			if (f.exists() && !flag) {
				// 已存在不覆盖
				System.out.println("文件已存在" + path);
				return;
			}
			// f.createNewFile();
			// f.deleteOnExit();

			out = new BufferedWriter(new FileWriter(f));
			out.write(content);
			out.flush();
			System.out.println("文件已创建" + path);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	/** 将指定文件按字符集读为String */
	public static String readerToString(String filename, String charset) throws IOException {
		BufferedReader is = new BufferedReader(new InputStreamReader(new FileInputStream(filename), charset));
		StringWriter sb = new StringWriter();
		int n;

		if (charset.equalsIgnoreCase("utf8") || charset.equalsIgnoreCase("utf-8")) {
			int count = 0;
			while ((n = is.read()) != -1) {
				if (count == 0 && Integer.toHexString(n).equalsIgnoreCase("feff")) {
					count++;
					continue;
				}

				sb.write(n);
			}
		} else {
			while ((n = is.read()) != -1) {
				sb.write(n);
			}
		}
		return sb.toString();
	}

	public static String streamToString(String fileName, String charset) throws IOException {
		StringBuffer sb = new StringBuffer();
		FileInputStream fis = new FileInputStream(fileName);

		InputStreamReader isr = new InputStreamReader(fis, charset);

		BufferedReader br = new BufferedReader(isr);

		String str;

		// System.out.println("File content:");

		while ((str = br.readLine()) != null)

			sb.append(str);
		System.out.print(sb);
		br.close();

		return sb.toString();
	}

	/**
	 * 从webroot的相应目录下取得文件
	 * 
	 * @param filename
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static String ulandFileToString(String filename, String charset) throws IOException {
		return readerToString(BDFileUtil.getWebRoot() + File.separator + filename, charset);
	}

	/**
	 * 文件复制
	 * 
	 * @param src
	 *            源
	 * @param dest
	 *            目标
	 */
	public static void copyFile(InputStream src, OutputStream dest) throws IOException {
		synchronized (src) {
			synchronized (dest) {
				byte[] buffer = new byte[BLKSIZ];
				while (true) {
					int bytesRead = src.read(buffer);
					if (bytesRead == -1)
						break;
					dest.write(buffer, 0, bytesRead);
				}
			}
		}
	}

	/**
	 * 文件复制
	 * 
	 * @param src
	 *            源
	 * @param dest
	 *            目标
	 */
	public static void copyFile(String src, String dest) throws FileNotFoundException, IOException {
		File infile = new File(src);
		File outfile = new File(dest);
		if (infile.getCanonicalPath().equals(outfile.getCanonicalPath())) {
			return;
		}
		BufferedInputStream is = new BufferedInputStream(new FileInputStream(infile));
		BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(outfile));
		copyFile(is, os);
		os.close();
		is.close();
	}

	/**
	 * 删除目录
	 * 
	 * @param path
	 */
	public static void delDir(String path) {
		File dir = new File(path);
		if (dir.exists()) {
			File[] tmp = dir.listFiles();
			for (int i = 0; i < tmp.length; i++) {
				if (tmp[i].isDirectory()) {
					delDir(path + "/" + tmp[i].getName());
				} else {
					tmp[i].delete();
				}
			}
			dir.delete();
		}
	}

	// 文件锁
	private static Object LOCK = BDFileUtil.class;

	/**
	 * 文件追加写入
	 * 
	 * @param path
	 *            文件路径
	 * @param data
	 *            追加内容
	 */
	public static void appendWrite(String path, byte[] data) {
		if (path == null || data == null) {
			return;
		}
		if (data.length == 0) {
			return;
		}
		synchronized (LOCK) {
			RandomAccessFile ranFile = null;
			try {
				ranFile = new RandomAccessFile(new File(path), "rw");
				long pos = ranFile.length();
				ranFile.seek(pos);
				ranFile.write(data);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (ranFile != null) {
					try {
						ranFile.close();
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			}
		}
	}
}
