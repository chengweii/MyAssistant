package weihua.myassistant.util;

import java.util.List;

import org.apache.log4j.Logger;

import com.google.gson.reflect.TypeToken;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class AmapUtil {

	private static Logger loger = Logger.getLogger(AmapUtil.class);

	private static final String key = "4f7b678a63bebec310250c56109aabb8";

	public static void main(String[] args) throws Exception {
		CircleParam param = new CircleParam();
		param.location = "116.572416,39.768533";
		param.radius = "500";
		Trafficstatus trafficstatus = AmapUtil.<Trafficstatus, CircleParam> getTrafficInfo(param);
		loger.info(trafficstatus);

		RectangleParam param1 = new RectangleParam();
		param1.lowerLeftCorner = "116.546519,39.76229";
		param1.upperRightCorner = "116.572416,39.768533";
		Trafficstatus trafficstatus1 = AmapUtil.<Trafficstatus, RectangleParam> getTrafficInfo(param1);
		loger.info(trafficstatus1);

		WalkParam param2 = new WalkParam();
		param2.origin = "116.546519,39.76229";
		param2.destination = "116.572416,39.768533";
		TrafficDirection trafficDirection2 = AmapUtil.<TrafficDirection, WalkParam> getTrafficInfo(param2);
		loger.info(trafficDirection2);

		DrivingParam param3 = new DrivingParam();
		param3.origin = "116.545811,39.761333";
		param3.destination = "116.565724,39.789937";
		param3.strategy = "2";
		param3.waypoints = "116.572236,39.768194";
		TrafficDirection trafficDirection3 = AmapUtil.<TrafficDirection, DrivingParam> getTrafficInfo(param3);
		loger.info(trafficDirection3);
	}

	public static <R extends Result, P extends Param> R getTrafficInfo(P param) throws Exception {
		R trafficResult = null;
		String apiUrl = null;
		String json = null;
		Call<ResponseBody> result = null;
		retrofit2.Response<ResponseBody> response = null;
		if (param instanceof WalkParam) {
			apiUrl = getApiWalkUrl((WalkParam) param);
			result = RetrofitUtil.retrofitService.get(apiUrl, "");
			response = result.execute();
			json = response.body().string();
			trafficResult = GsonUtil.getEntityFromJson(json, new TypeToken<TrafficDirection>() {
			});
		} else if (param instanceof DrivingParam) {
			apiUrl = getApiDrivingUrl((DrivingParam) param);
			result = RetrofitUtil.retrofitService.get(apiUrl, "");
			response = result.execute();
			json = response.body().string();
			trafficResult = GsonUtil.getEntityFromJson(json, new TypeToken<TrafficDirection>() {
			});
		} else if (param instanceof CircleParam) {
			apiUrl = getApiCircleUrl((CircleParam) param);
			result = RetrofitUtil.retrofitService.get(apiUrl, "");
			response = result.execute();
			json = response.body().string();
			trafficResult = GsonUtil.getEntityFromJson(json, new TypeToken<Trafficstatus>() {
			});
		} else if (param instanceof RectangleParam) {
			apiUrl = getApiRectangleUrl((RectangleParam) param);
			result = RetrofitUtil.retrofitService.get(apiUrl, "");
			response = result.execute();
			json = response.body().string();
			trafficResult = GsonUtil.getEntityFromJson(json, new TypeToken<Trafficstatus>() {
			});
		}

		return trafficResult;

	}

	/* 规划查询begin */

	private static String getApiWalkUrl(WalkParam param) {
		return "http://restapi.amap.com/v3/direction/walking?key=" + key + "&origin=" + param.origin + "&destination="
				+ param.destination;
	}

	private static String getApiDrivingUrl(DrivingParam param) {
		return "http://restapi.amap.com/v3/direction/driving?key=" + key + "&origin=" + param.origin + "&destination="
				+ param.destination + "&extensions=" + param.extensions + "&strategy=" + param.strategy + "&waypoints="
				+ param.waypoints + "&avoidpolygons=" + param.avoidpolygons;
	}

	public static class WalkParam implements Param {
		public String origin;
		public String destination;
	}

	public static class DrivingParam implements Param {
		public String origin;
		public String destination;
		public String extensions = "base";
		public String strategy = "0";
		public String waypoints;
		public String avoidpolygons;
	}

	public static class TrafficDirection implements Result {
		public String status;
		public String info;
		public String infocode;
		public Route route;

		public static class Route {
			public String origin;
			public String destination;
			public List<Path> paths;

			public static class Path {
				public String distance;
				public String duration;
				public List<Step> steps;

				public static class Step {
					public String instruction;
					public String road;
					public String distance;
					public String duration;
					public String polyline;
				}
			}
		}
	}

	/* 规划查询end */

	/* 路况查询begin */

	private static String getApiRectangleUrl(RectangleParam param) {
		return "http://restapi.amap.com/v3/traffic/status/rectangle?key=" + key + "&rectangle=" + param.lowerLeftCorner
				+ ";" + param.upperRightCorner;
	}

	private static String getApiCircleUrl(CircleParam param) {
		return "http://restapi.amap.com/v3/traffic/status/circle?key=" + key + "&location=" + param.location
				+ "&radius=" + param.radius;
	}

	public static class RectangleParam implements Param {
		public String lowerLeftCorner;
		public String upperRightCorner;
	}

	public static class CircleParam implements Param {
		public String location;
		public String radius;
	}

	public static class Trafficstatus implements Result {
		public String status;
		public String info;
		public String infocode;
		public Trafficinfo trafficinfo;

		public static class Trafficinfo {
			public String description;
			public Evaluation evaluation;

			public static class Evaluation {
				public String expedite;
				public String congested;
				public String blocked;
				public String unknown;
				public String status;
				public String description;
			}
		}
	}

	/* 路况查询end */

	public interface Result {

	}

	public interface Param {

	}
}
