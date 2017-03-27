@ResponseBody
	@RequestMapping("/test")
	public Map<String, Object> test(HttpServletRequest request, Map<String, Object> data) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		Map<String, String[]> paramMap = request.getParameterMap();
		Set<Map.Entry<String, String[]>> set = paramMap.entrySet();
		Iterator<Map.Entry<String, String[]>> ite = set.iterator();
		while (ite.hasNext()) {
			Map.Entry<String, String[]> temp = ite.next();
			String key = temp.getKey();
			String value = "";
			String[] valueString = temp.getValue();
			for (int i = 0; i < valueString.length; i++) {
				value = value + valueString[i] + ",";
			}
			value = value.substring(0, value.length() - 1);
			param.put(key, value);
		}
		result.put("info", param);

		return result;
	}