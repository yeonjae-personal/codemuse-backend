package com.lgcns.svcp.prod.online.prod.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lgcns.svcp.prod.online.prod.dto.characteristic.BlngInfoMDto;
import com.lgcns.svcp.prod.online.prod.dto.characteristic.DcntCstcMDto;
import com.lgcns.svcp.prod.online.prod.dto.characteristic.DcntTrgtInfoDDto;
import com.lgcns.svcp.prod.online.prod.dto.characteristic.DcntTrgtInfoMDto;
import com.lgcns.svcp.prod.online.prod.dto.characteristic.DtexPlcyMDto;
import com.lgcns.svcp.prod.online.prod.dto.characteristic.LimMDto;
import com.lgcns.svcp.prod.online.prod.dto.characteristic.LobMDto;
import com.lgcns.svcp.prod.online.prod.dto.characteristic.LobMrktRelDDto;
import com.lgcns.svcp.prod.online.prod.dto.characteristic.QosMDto;
import com.lgcns.svcp.prod.online.prod.dto.characteristic.QosPlcyRelDDto;
import com.lgcns.svcp.prod.online.prod.dto.characteristic.SlinInfoMDto;
import com.lgcns.svcp.prod.online.prod.dto.characteristic.SpamLvwuPlcyRelDDto;
import com.lgcns.svcp.prod.online.prod.dto.characteristic.SpamMDto;

import com.lgcns.svcp.prod.dataaccess.CommonDao;

@Component
public class CharacteristicService {
	@Autowired
	private CommonDao commonDao;
	
	public List<BlngInfoMDto> retrieveBlngInfoMList(BlngInfoMDto blngInfoMDto) {
		List<BlngInfoMDto> resultList = commonDao.selectList("Characteristic.retrieveBlngInfoMList", blngInfoMDto);
		return resultList;
		}

		public BlngInfoMDto retrieveBlngInfoM(BlngInfoMDto blngInfoMDto) {
		return commonDao.select("Characteristic.retrieveBlngInfoM", blngInfoMDto);
		}

		public List<DcntCstcMDto> retrieveDcntCstcMList(DcntCstcMDto dcntCstcMDto) {
		List<DcntCstcMDto> resultList = commonDao.selectList("Characteristic.retrieveDcntCstcMList", dcntCstcMDto);
		return resultList;
		}

		public DcntCstcMDto retrieveDcntCstcM(DcntCstcMDto dcntCstcMDto) {
		return commonDao.select("Characteristic.retrieveDcntCstcM", dcntCstcMDto);
		}

		public List<DcntTrgtInfoMDto> retrieveDcntTrgtInfoMList(DcntTrgtInfoMDto dcntTrgtInfoMDto) {
		List<DcntTrgtInfoMDto> resultList = commonDao.selectList("Characteristic.retrieveDcntTrgtInfoMList", dcntTrgtInfoMDto);
		return resultList;
		}

		public DcntTrgtInfoMDto retrieveDcntTrgtInfoM(DcntTrgtInfoMDto dcntTrgtInfoMDto) {
		return commonDao.select("Characteristic.retrieveDcntTrgtInfoM", dcntTrgtInfoMDto);
		}

		public List<DcntTrgtInfoDDto> retrieveDcntTrgtInfoDList(DcntTrgtInfoDDto dcntTrgtInfoDDto) {
		List<DcntTrgtInfoDDto> resultList = commonDao.selectList("Characteristic.retrieveDcntTrgtInfoDList", dcntTrgtInfoDDto);
		return resultList;
		}

		public DcntTrgtInfoDDto retrieveDcntTrgtInfoD(DcntTrgtInfoDDto dcntTrgtInfoDDto) {
		return commonDao.select("Characteristic.retrieveDcntTrgtInfoD", dcntTrgtInfoDDto);
		}

		public List<DtexPlcyMDto> retrieveDtexPlcyMList(DtexPlcyMDto dtexPlcyMDto) {
		List<DtexPlcyMDto> resultList = commonDao.selectList("Characteristic.retrieveDtexPlcyMList", dtexPlcyMDto);
		return resultList;
		}

		public DtexPlcyMDto retrieveDtexPlcyM(DtexPlcyMDto dtexPlcyMDto) {
		return commonDao.select("Characteristic.retrieveDtexPlcyM", dtexPlcyMDto);
		}

		public List<LimMDto> retrieveLimMList(LimMDto limMDto) {
		List<LimMDto> resultList = commonDao.selectList("Characteristic.retrieveLimMList", limMDto);
		return resultList;
		}

		public LimMDto retrieveLimM(LimMDto limMDto) {
		return commonDao.select("Characteristic.retrieveLimM", limMDto);
		}

		public List<LobMDto> retrieveLobMList(LobMDto lobMDto) {
		List<LobMDto> resultList = commonDao.selectList("Characteristic.retrieveLobMList", lobMDto);
		return resultList;
		}

		public LobMDto retrieveLobM(LobMDto lobMDto) {
		return commonDao.select("Characteristic.retrieveLobM", lobMDto);
		}

		public List<LobMrktRelDDto> retrieveLobMrktRelDList(LobMrktRelDDto lobMrktRelDDto) {
		List<LobMrktRelDDto> resultList = commonDao.selectList("Characteristic.retrieveLobMrktRelDList", lobMrktRelDDto);
		return resultList;
		}

		public LobMrktRelDDto retrieveLobMrktRelD(LobMrktRelDDto lobMrktRelDDto) {
		return commonDao.select("Characteristic.retrieveLobMrktRelD", lobMrktRelDDto);
		}

		public List<QosMDto> retrieveQosMList(QosMDto qosMDto) {
		List<QosMDto> resultList = commonDao.selectList("Characteristic.retrieveQosMList", qosMDto);
		return resultList;
		}

		public QosMDto retrieveQosM(QosMDto qosMDto) {
		return commonDao.select("Characteristic.retrieveQosM", qosMDto);
		}

		public List<QosPlcyRelDDto> retrieveQosPlcyRelDList(QosPlcyRelDDto qosPlcyRelDDto) {
		List<QosPlcyRelDDto> resultList = commonDao.selectList("Characteristic.retrieveQosPlcyRelDList", qosPlcyRelDDto);
		return resultList;
		}

		public QosPlcyRelDDto retrieveQosPlcyRelD(QosPlcyRelDDto qosPlcyRelDDto) {
		return commonDao.select("Characteristic.retrieveQosPlcyRelD", qosPlcyRelDDto);
		}

		public List<SlinInfoMDto> retrieveSlinInfoMList(SlinInfoMDto slinInfoMDto) {
		List<SlinInfoMDto> resultList = commonDao.selectList("Characteristic.retrieveSlinInfoMList", slinInfoMDto);
		return resultList;
		}

		public SlinInfoMDto retrieveSlinInfoM(SlinInfoMDto slinInfoMDto) {
		return commonDao.select("Characteristic.retrieveSlinInfoM", slinInfoMDto);
		}

		public List<SpamMDto> retrieveSpamMList(SpamMDto spamMDto) {
		List<SpamMDto> resultList = commonDao.selectList("Characteristic.retrieveSpamMList", spamMDto);
		return resultList;
		}

		public SpamMDto retrieveSpamM(SpamMDto spamMDto) {
		return commonDao.select("Characteristic.retrieveSpamM", spamMDto);
		}

		public List<SpamLvwuPlcyRelDDto> retrieveSpamLvwuPlcyRelDList(SpamLvwuPlcyRelDDto spamLvwuPlcyRelDDto) {
		List<SpamLvwuPlcyRelDDto> resultList = commonDao.selectList("Characteristic.retrieveSpamLvwuPlcyRelDList", spamLvwuPlcyRelDDto);
		return resultList;
		}

		public SpamLvwuPlcyRelDDto retrieveSpamLvwuPlcyRelD(SpamLvwuPlcyRelDDto spamLvwuPlcyRelDDto) {
		return commonDao.select("Characteristic.retrieveSpamLvwuPlcyRelD", spamLvwuPlcyRelDDto);
		}

}
