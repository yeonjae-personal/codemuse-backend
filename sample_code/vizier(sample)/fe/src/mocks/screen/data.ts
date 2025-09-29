export const generateMockDataScreens = (numRecords: number) => {
  const mockData = [];
  for (let i = 1; i <= numRecords; i++) {
    mockData.push({
      screenNo: i,
      screenId: `SCR${String(i).padStart(3, '0')}`,
      screenNm: `Screen Name ${i}`,
      screenDesc: `Description for screen ${i}`,
      screenLinkUrl: `/screen/${i}`,
      screenPath: `/screen/${i}`,
      rgstUsr: `user${i}`,
      enable: i % 2 === 0,
      updDtm: `2023-10-${String(i).padStart(2, '0')}`,
    });
  }
  return mockData;
};

export const generateMockDataUrls = (numRecords: number) => {
  const mockData = [];
  const methods = ['GET', 'POST', 'PUT', 'DELETE'];
  for (let i = 1; i <= numRecords; i++) {
    mockData.push({
      urlNo: i,
      url: `/api/resource/${i}`,
      method: methods[i % methods.length],
      urlNm: `Resource ${i}`,
      urlDesc: `Description for resource ${i}`,
      rgstUsr: `user${i}`,
      updDtm: `2023-10-${String(i).padStart(2, '0')}`,
    });
  }
  return mockData;
};