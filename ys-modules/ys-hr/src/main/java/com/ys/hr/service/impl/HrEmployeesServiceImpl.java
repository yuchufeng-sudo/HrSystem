package com.ys.hr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.common.core.constant.SecurityConstants;
import com.ys.common.core.exception.ServiceException;
import com.ys.common.core.utils.DateUtils;
import com.ys.common.core.utils.StringUtils;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.WebUrl;
import com.ys.hr.domain.*;
import com.ys.hr.domain.excel.HrEmployeesExcel;
import com.ys.hr.domain.vo.*;
import com.ys.hr.mapper.*;
import com.ys.hr.service.*;
import com.ys.system.api.RemoteMessageService;
import com.ys.system.api.domain.SysMessage;
import com.ys.system.api.domain.SysUser;
import com.ys.system.api.domain.SysUserRole;
import com.ys.utils.email.EmailUtils;
import com.ys.utils.random.RandomStringGenerator;
import org.apache.commons.lang3.ObjectUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 *  Employee ServiceBusiness layer processing
 *
 * @author ys
 * @date 2025-05-21
 */
@Service
public class HrEmployeesServiceImpl extends ServiceImpl<HrEmployeesMapper, HrEmployees> implements IHrEmployeesService
{

    @Resource
    private HrEmpHolidayMapper hrEmpHolidayMapper;

    @Resource
    private HrDeptMapper hrDeptMapper;

    @Resource
    private EmailUtils emailUtils;

    @Resource
    private IHrDocumentService documentService;

    @Resource
    private IHrDocumentShareService documentShareService;

    @Resource
    private IHrTargetsService targetsService;

    @Resource
    private IHrTargetTasksService targetTasksService;

    @Resource
    private HrCandidateInfoMapper candidateInfoMapper;

    @Resource
    private RemoteMessageService remoteMessageService;

    @Resource
    private WebUrl webUrl;

    /**
     * Query THE Employee list.
     *
     *
     * @param hrEmployees  Employee
     * @return Employee
     */
    @Override
    public List<HrEmployees> selectHrEmployeesList(HrEmployees hrEmployees)
    {
        return baseMapper.selectHrEmployeesList(hrEmployees);
    }

    @Override
    public List<HrEmployeesExcel> selectHrEmployeesExcelList(HrEmployees hrEmployees)
    {
        List<HrEmployees> hrEmployees1 = baseMapper.selectHrEmployeesList(hrEmployees);
        List<HrEmployeesExcel> list = new ArrayList<>();
        for (HrEmployees employees : hrEmployees1) {
            HrEmployeesExcel hrEmployeesExcel = new HrEmployeesExcel();
            hrEmployeesExcel.setFullName(employees.getFullName());
            hrEmployeesExcel.setBankName(employees.getBankName());
            hrEmployeesExcel.setBankNumber(employees.getBankNumber());
            hrEmployeesExcel.setBaseSalary(employees.getBaseSalary());
            hrEmployeesExcel.setContactPhone(employees.getContactPhone());
            hrEmployeesExcel.setEmail(employees.getEmail());
            hrEmployeesExcel.setHireDate(employees.getHireDate());
            hrEmployeesExcel.setPosition(employees.getPositionName());
            hrEmployeesExcel.setAccuFundIdCost(employees.getAccuFundIdCost());
            hrEmployeesExcel.setComInsuIdCost(employees.getComInsuIdCost());
            hrEmployeesExcel.setDateOfBirth(employees.getDateOfBirth());
            hrEmployeesExcel.setPhoneCode(employees.getPhoneCode());
            hrEmployeesExcel.setSocialSecurityCost(employees.getSocialSecurityCost());
            hrEmployeesExcel.setEmploymentType(employees.getEmploymentType());
            hrEmployeesExcel.setAccountLevel(employees.getAccessLevel());
            hrEmployeesExcel.setStatus(employees.getStatus());
            list.add(hrEmployeesExcel);
        }
        return list;
    }

    @Override
    public HrEmployees selectHrEmployeesByUserId(Long userId) {
        return baseMapper.selectHrEmployeesByUserId(userId);
    }

    @Override
    public HrEmployees selectHrEmployeesById(Long id){
        return baseMapper.selectHrEmployeesById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long insertEmployees(HrEmployees hrEmployees) {
        selectSysUserCountByUsernamesOrEmailOrWorkNoWithLock(hrEmployees);
        SysUser sysUser = new SysUser();
        sysUser.setUserName(hrEmployees.getUsername());
        Long candidateId = hrEmployees.getCandidateId();
        String userEnterpriseId = SecurityUtils.getUserEnterpriseId();
        if (hrEmployees.getEnterpriseId()==null || hrEmployees.getEnterpriseId().isEmpty()) {
            sysUser.setEnterpriseId(userEnterpriseId);
        }else {
            sysUser.setEnterpriseId(hrEmployees.getEnterpriseId());
        }
        String password = "";
        if (hrEmployees.getPassword()==null || hrEmployees.getPassword().isEmpty()){
            password = RandomStringGenerator.generate16BitRandomString();
        }else{
            password = hrEmployees.getPassword();
        }
        sysUser.setPassword(SecurityUtils.encryptPassword(password));
        if (hrEmployees.getAvatarUrl()==null) {
            hrEmployees.setAvatarUrl("https://ycfeng.oss-cn-beijing.aliyuncs.com/ycf/default-avatar.jpg");
        }
        String userType = hrEmployees.getUserType();
        if (userType!=null) {
            sysUser.setUserType(userType);
        }else {
            sysUser.setUserType("02");
        }
        sysUser.setPhoneCode(hrEmployees.getPhoneCode());
        sysUser.setNickName(hrEmployees.getFullName());
        sysUser.setEmail(hrEmployees.getEmail());
        sysUser.setStatus("0");
        sysUser.setAvatar(hrEmployees.getAvatarUrl());
        baseMapper.insertSysUser(sysUser);

        HrDocument hrDocument = new HrDocument();
        hrDocument.setUploadCandidateId(candidateId);
        List<HrDocument> hrDocuments = documentService.selectHrDocumentList(hrDocument);
        if (!hrDocuments.isEmpty()) {
            QueryWrapper<HrDocumentShare> queryWrapper = new QueryWrapper<>();
            Long documentId = hrDocuments.get(0).getDocumentId();
            HrDocumentServiceImpl.saveShareDocument(documentShareService, queryWrapper, documentId, sysUser.getUserId());
        }

        hrEmployees.setUserId(sysUser.getUserId());
        hrEmployees.setEnterpriseId(sysUser.getEnterpriseId());
        setSysRole(hrEmployees);
        hrEmployees.setHireDate(DateUtils.getNowDate());
        int insert = baseMapper.insert(hrEmployees);
        if(insert>0){
            String companyName = baseMapper.seleEid(SecurityUtils.getUserEnterpriseId());
            Map<String, Object> map = new HashMap<>();
            map.put("FirstName",hrEmployees.getFullName());
            map.put("CompanyName",companyName);
            String userName = hrEmployees.getUsername();
            map.put("Username",userName);
            map.put("Password",password);
            map.put("LoginUrl",webUrl.getUrl());
            emailUtils.sendEmailByTemplate(map,hrEmployees.getEmail(),"EmployeeAccountCreatedManually");
        }
        return hrEmployees.getEmployeeId();
    }

    @Override
    @Transactional
    public Long updateEmployees(HrEmployees hrEmployees) {
        Long employeeId = hrEmployees.getEmployeeId();
        HrEmployees hrEmployees1 = baseMapper.selectHrEmployeesById(employeeId);
        boolean a = hrEmployees.getFullName() != null && !hrEmployees1.getFullName().equals(hrEmployees.getFullName());
        boolean b = hrEmployees.getEmail() != null && !hrEmployees1.getEmail().equals(hrEmployees.getEmail());
        boolean c = hrEmployees.getJobnumber() != null && !hrEmployees1.getJobnumber().equals(hrEmployees.getJobnumber());
        if (a||b){
            SysUser sysUser = new SysUser();
            sysUser.setUserId(hrEmployees1.getUserId());
            if (a){
                sysUser.setNickName(hrEmployees.getFullName());
            }
            if (b){
                checkEmail(hrEmployees.getEmail());
                sysUser.setEmail(hrEmployees.getEmail());
            }
            baseMapper.updateSysUser(sysUser);
        }
        if (c){
            checkWorkNo(hrEmployees.getJobnumber());
        }
        if (hrEmployees.getAccessLevel()!=null&&hrEmployees1.getAccessLevel().equals(hrEmployees.getAccessLevel())) {
            baseMapper.deleteUserRoleByUserId(hrEmployees1.getUserId());
            setSysRole(hrEmployees);
        }
        hrEmployees.setUserId(null);
        hrEmployees.setEnterpriseId(null);
        if (hrEmployees.getDeptId()!=null && (hrEmployees1.getDeptId() == null || !hrEmployees1.getDeptId().equals(hrEmployees.getDeptId()))) {
            hrDeptMapper.deleteHrDeptEmployeesByEmployeesId(employeeId);
            HrDeptEmployees hrDeptEmployees = new HrDeptEmployees();
            hrDeptEmployees.setEmployeeId(employeeId);
            hrDeptEmployees.setDeptId(hrEmployees.getDeptId());
            List<HrDeptEmployees> list = new ArrayList<>();
            list.add(hrDeptEmployees);
            hrDeptMapper.batchHrDeptEmployees(list);
        }
        baseMapper.updateById(hrEmployees);
        return employeeId;
    }

    private void selectSysUserCountByUsernamesOrEmailOrWorkNoWithLock(HrEmployees hrEmployees) {
        if (hrEmployees.getUsername()!=null){
            checkUsername(hrEmployees.getUsername());
        }
        if (hrEmployees.getEmail()!=null){
            checkEmail(hrEmployees.getEmail());
        }
        if (hrEmployees.getJobnumber()!=null){
            checkWorkNo(hrEmployees.getJobnumber());
        }
    }

    private void checkEmail(String emails) {
        int i3 = baseMapper.selectSysUserCountByEmail(emails).size();
        if (i3!=0){
            throw new ServiceException("Email must be unique.");
        }
    }

    private void checkWorkNo(String jobnumber) {
        int i3 = baseMapper.selectEmployeeCountByWorkNo(jobnumber, SecurityUtils.getUserEnterpriseId());
        if (i3!=0){
            throw new ServiceException("Job number " + jobnumber + " already exists");
        }
    }

    private void checkUsername(String usernames) {
        int i1 = baseMapper.selectSysUserCountByUsername(usernames).size();
        if (i1!=0){
            throw new ServiceException("Login username must be unique.");
        }
    }

    private void setSysRole(HrEmployees hrEmployees) {
        SysUserRole sysUserRole = new SysUserRole();
        Long roleId = Long.valueOf(hrEmployees.getAccessLevel());
        sysUserRole.setRoleId(roleId);
        sysUserRole.setUserId(hrEmployees.getUserId());
        baseMapper.insertUserRole(sysUserRole);
    }

    @Override
    public List<EmergencyContactsReportVo> selectEmergencyContactsReportVo(HrEmployees hrEmployees) {
        return baseMapper.selectEmergencyContactsReportVo(hrEmployees);
    }

    @Override
    public List<BirthdayReportVo> selectBirthdayReportVoReportVo(HrEmployees hrEmployees) {
        return baseMapper.selectBirthdayReportVoReportVo(hrEmployees);
    }

    /**
     *
     * @param userEnterpriseId
     * @return
     */
    @Override
    public Map<String, Object> getEmployeeStatusCount(String userEnterpriseId) {
        DecimalFormat df = new DecimalFormat("0.00");
        EmployeementStatusVo vo = baseMapper.selectEmployeeStatusCount(userEnterpriseId);
        Long totalCount = vo.getFullTime() + vo.getPartTime() + vo.getInternShip();
        List<StatusData> list = new ArrayList<>();
        if (totalCount == 0){
            list.add(new StatusData("Full-time", vo.getFullTime(), df.format(0), "#FF7A2E"));
            list.add(new StatusData("Part-time", vo.getPartTime(),  df.format(0), "#F8AF6A"));
            list.add(new StatusData("Internship", vo.getInternShip(), df.format(0), "#0CAA5E"));
        }else {
            list.add(new StatusData("Full-time", vo.getFullTime(), df.format((vo.getFullTime().doubleValue() / totalCount.doubleValue()) * 100), "#FF7A2E"));
            list.add(new StatusData("Part-time", vo.getPartTime(),  df.format((vo.getPartTime().doubleValue() / totalCount.doubleValue()) * 100), "#F8AF6A"));
            list.add(new StatusData("Internship", vo.getInternShip(), df.format((vo.getInternShip().doubleValue() / totalCount.doubleValue()) * 100), "#0CAA5E"));
        }
        Map<String, Object> map = new HashMap<>();
        map.put("totalCount", totalCount);
        map.put("list", list);
        return map;
    }

    /**
     * Query Employee QUANTITY
     * @param userEnterpriseId
     * @return
     */
    @Override
    public Map<String, Object> selectEmployeeCount(String userEnterpriseId) {
        EmployeeCountVo employeeCountVo = baseMapper.selectEmployeeCount(userEnterpriseId);
        Map<String, Object> map = new HashMap<>();
        map.put("totalCount", employeeCountVo.getTotalCount());
        if (employeeCountVo.getTotalCount() > employeeCountVo.getLastMonthCount()) {
            // Rising
            map.put("ratioType", 1);
        } else if (employeeCountVo.getTotalCount() < employeeCountVo.getLastMonthCount()) {
            // Falling
            map.put("ratioType", 2);
        } else {
            // Flat
            map.put("ratioType", 1);
        }
        BigDecimal thisMonth = BigDecimal.valueOf(employeeCountVo.getTotalCount());
        BigDecimal lastMonth = BigDecimal.valueOf(employeeCountVo.getLastMonthCount());
        if (lastMonth.compareTo(BigDecimal.ZERO) == 0){
            map.put("ratio", 100);
        }else {
            // (this - last) / last * 100
            BigDecimal diff = thisMonth.subtract(lastMonth);
            BigDecimal ratio = diff
                    .divide(lastMonth, 2, RoundingMode.HALF_UP)
                    // Calculate the ratio first, keep 2 decimal places
                    .multiply(BigDecimal.valueOf(100));
            // Multiply by 100 to get the percentage
            map.put("ratio", ratio);
        }
        return map;
    }

    /**
     * Query Employee Birthdays
     * @param hrEmployees
     * @return
     */
    @Override
   public List<BirthdayReportVo> selectEmployeeBirthday(HrEmployees hrEmployees) {
        // 2. Define the target format: Day of week abbreviation EEE, day dd, month abbreviation MMM
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE dd MMM", Locale.ENGLISH);

        List<BirthdayReportVo> list = baseMapper.selectEmployeeBirthday(hrEmployees);
        list.forEach(birthdayReportVo -> {
            if (ObjectUtils.isNotEmpty(birthdayReportVo.getDateOfBirth())){
                // 1. Convert to ZonedDateTime (using the system's default time zone)
                ZonedDateTime zdt = birthdayReportVo.getDateOfBirth().toInstant()
                        .atZone(ZoneId.systemDefault());
                birthdayReportVo.setMonth(zdt.format(formatter));
            }else {
                birthdayReportVo.setMonth("No birthday information");
            }
        });
        return list;
    }

    /**
     * Celebration
     * @param userId
     * @return
     */
    @Override
   public List<CelebrationVo> selectCelebrationList(Long userId) {
        // Obtain today's date
        LocalDate today = LocalDate.now();
        List<CelebrationVo> list = new ArrayList<>();
        HrEmployees hrEmployees = baseMapper.selectHrEmployeesByUserId(userId);
        if (ObjectUtils.isNotEmpty(hrEmployees) && StringUtils.isNotEmpty(hrEmployees.getEnterpriseId())){
            List<HrEmployees> employeesList = baseMapper.selectEmployeesListByEid(hrEmployees.getEnterpriseId());
            employeesList.stream().forEach(employee -> {
                if (ObjectUtils.isNotEmpty(employee.getDateOfBirth())){
                    CelebrationVo celebrationVo = new CelebrationVo();
                    Date dateOfBirth = employee.getDateOfBirth();
                    // Convert Date to LocalDate
                    LocalDate targetLocalDate = Instant.ofEpochMilli(dateOfBirth.getTime())
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();
                    // Determine if the month and day are the same
                    boolean isSameMonthDay = today.getMonthValue() == targetLocalDate.getMonthValue()
                            && today.getDayOfMonth() == targetLocalDate.getDayOfMonth();
                    if (isSameMonthDay){
                        celebrationVo.setType("1");
                        celebrationVo.setTitle("Birthday celebration");
                        celebrationVo.setDescription(employee.getFullName() + " has a birthday today");
                        list.add(celebrationVo);
                    }
                }
            });

            List<HrEmpHoliday> list1 = hrEmpHolidayMapper.selectHrEmpHolidayListBYToday(hrEmployees.getEnterpriseId());
            list1.forEach(hrEmpHoliday -> {
                CelebrationVo celebrationVo = new CelebrationVo();
                celebrationVo.setType("2");
                celebrationVo.setTitle(hrEmpHoliday.getHolidayName());
                celebrationVo.setDescription("The company is celebrating " + hrEmpHoliday.getHolidayName());
                list.add(celebrationVo);
            });
        }
        return list;
    }

    @Override
    public int deleteById(Long employeeId)
    {
        HrEmployees hrEmployees = baseMapper.selectById(employeeId);
        Long userId = hrEmployees.getUserId();
        baseMapper.deleteSysUserById(userId);
        return baseMapper.deleteById(employeeId);
    }

    @Transactional
    @Override
    public int resignEmployee(Long employeeId) {
        return resignEmployee(employeeId,null);
    }

    public int resignEmployee(Long employeeId, Long resignationHrUserId) {
        HrEmployees hrEmployees = baseMapper.selectById(employeeId);
        Long userId = hrEmployees.getUserId();
        if (resignationHrUserId==null){
            resignationHrUserId = hrEmployees.getResignationHrUserId();
        }
        HrEmployees hrEmployees2 = baseMapper.selectHrEmployeesByUserId(resignationHrUserId);
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setStatus("1");
        baseMapper.updateSysUser(sysUser);
        HrEmployees hrEmployees1 = new HrEmployees();
        hrEmployees1.setEmployeeId(employeeId);
        hrEmployees1.setStatus("2");
        HrEnterprise hrEnterprise = candidateInfoMapper.seleEid(hrEmployees.getEnterpriseId());
        Map<String, Object> map = new HashMap<>();
        map.put("FullName",hrEmployees.getFullName());
        map.put("HrisToolName",HRIS_TOOL_NAME);
        map.put("HrEmail",hrEmployees2.getEmail());
        map.put("CompanyName",hrEnterprise.getEnterpriseName());
        emailUtils.sendEmailByTemplate(map,hrEmployees.getEmail(),"TerminationComplete");
        SysMessage sysMessage = getSysMessage(employeeId, resignationHrUserId, hrEmployees);
        remoteMessageService.sendMessageByTemplate(sysMessage, SecurityConstants.INNER);
        return baseMapper.updateById(hrEmployees1);
    }

    @NotNull
    private static SysMessage getSysMessage(Long employeeId, Long resignationHrUserId, HrEmployees hrEmployees) {
        SysMessage sysMessage = new SysMessage();
        sysMessage.setMessageRecipient(resignationHrUserId);
        sysMessage.setMessageStatus("0");
        sysMessage.setMessageType(21);
        sysMessage.setCreateTime(DateUtils.getNowDate());
        Map<String, Object> map1 = new HashMap<>();
        Map<String, Object> map2 = new HashMap<>();
        QueryWrapper<HrTargets> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("resign_employee_id", employeeId);
        queryWrapper.eq("name",OFFBOARDING_TARGET_HR);
        map1.put("fullName", hrEmployees.getFullName());
        sysMessage.setMap1(map1);
        sysMessage.setMap2(map2);
        return sysMessage;
    }

    @Transactional
    @Override
    public int resignEmployees(HrEmployees employees) {
        Long employeeId = employees.getEmployeeId();
        Long userId1 = SecurityUtils.getUserId();
        HrEmployees hrEmployees2 = baseMapper.selectHrEmployeesByUserId(userId1);
//        SysUser sysUser = new SysUser();
//        sysUser.setUserId(userId);
//        sysUser.setStatus("1");
//        baseMapper.updateSysUser(sysUser);
        Date resignationDate = employees.getResignationDate();
        String systemAccess = employees.getSystemAccess();
        if (systemAccess==null|| "2".equals(systemAccess)) {
            return this.resignEmployee(employeeId,SecurityUtils.getUserId());
        }else {
            Long employeeId1 = hrEmployees2.getEmployeeId();
            createTargets(employeeId,employeeId1,userId1,resignationDate);
        }
        HrEmployees hrEmployees1 = getHrEmployees(employees);
        return baseMapper.updateById(hrEmployees1);
    }

    private void createTargets(Long employeeId, Long employeeId1, Long userId, Date resignationDate) {
        HrEmployees hrEmployees = baseMapper.selectById(employeeId);
        HrTargets hrTargets = createTarget(employeeId, userId, resignationDate, OFFBOARDING_TARGET_HR);
        hrTargets.setEmployeeIds(new Long[]{employeeId1});
        targetsService.insertHrTargets(hrTargets);
        HrTargets hrTargets1 = createTarget(employeeId, userId, resignationDate, OFFBOARDING_TARGET_STAFF);
        hrTargets1.setEmployeeIds(new Long[]{employeeId});
        targetsService.insertHrTargets(hrTargets1);
        List<String> list = new ArrayList<>();
        list.add("Complete clearance");
        list.add("Return equipment");
        list.add("Return cash on hand");
        list.add("Management approval");
        list.add("Coordinate access removal");
        list.add("Confirm bond agreement");
        list.add("Issue separation form");
        this.insertHrTargetTasks(hrTargets.getId(),list);
        List<String> list1 = new ArrayList<>();
        list1.add("Complete clearance");
        list1.add("Return equipment");
        list1.add("Return cash on hand");
        list1.add("Complete handover of tasks");
        list1.add("Sign separation form");
        this.insertHrTargetTasks(hrTargets1.getId(),list1);
        HrEnterprise hrEnterprise = candidateInfoMapper.seleEid(SecurityUtils.getUserEnterpriseId());
        Map<String, Object> map = new HashMap<>();
        map.put("FullName",hrEmployees.getFullName());
        map.put("HrisToolName","Shiftcare HR");
        map.put("OffboardingUrl", webUrl.getUrl() +"/targets/detail?id="+hrTargets1.getId());
        map.put("CompanyName",hrEnterprise.getEnterpriseName());
        emailUtils.sendEmailByTemplate(map,hrEmployees.getEmail(),"OffboardingInitiated");
        SysMessage sysMessage = buildMessage(hrEmployees, hrTargets);
        remoteMessageService.sendMessageByTemplate(sysMessage, SecurityConstants.INNER);
    }

    @NotNull
    private static SysMessage buildMessage(HrEmployees hrEmployees, HrTargets hrTargets) {
        SysMessage sysMessage = new SysMessage();
        sysMessage.setMessageRecipient(SecurityUtils.getUserId());
        sysMessage.setMessageStatus("0");
        sysMessage.setMessageType(19);
        sysMessage.setCreateTime(DateUtils.getNowDate());
        Map<String, Object> map1 = new HashMap<>();
        Map<String, Object> map2 = new HashMap<>();
        map1.put("fullName", hrEmployees.getFullName());
        map2.put("id", hrTargets.getId());
        sysMessage.setMap1(map1);
        sysMessage.setMap2(map2);
        return sysMessage;
    }

    private HrTargets createTarget(Long employeeId, Long userId, Date resignationDate, String offboardingTargetHr) {
        HrTargets hrTargets = new HrTargets();
        hrTargets.setName(offboardingTargetHr);
        hrTargets.setDescription(offboardingTargetHr);
        hrTargets.setType("Individual Goal");
        hrTargets.setStartTime(DateUtils.getNowDate());
        hrTargets.setEndTime(resignationDate);
        hrTargets.setHead(userId);
        hrTargets.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        hrTargets.setStatus("Not Started");
        hrTargets.setResignEmployeeId(employeeId);
        return hrTargets;
    }

    @Override
    public int resignCancelEmployees(HrEmployees employees) {
        Long employeeId = employees.getEmployeeId();
        HrEmployees employees1 = new HrEmployees();
        employees1.setEmployeeId(employeeId);
        employees1.setStatus("1");
        QueryWrapper<HrTargets> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("resign_employee_id",employeeId);
        targetsService.remove(queryWrapper);
        return baseMapper.updateById(employees1);
    }

    private void insertHrTargetTasks(Long targetId,List<String> tasks){
        for (String task : tasks) {
            HrTargetTasks hrTargetTasks = new HrTargetTasks();
            hrTargetTasks.setTargetId(targetId);
            hrTargetTasks.setStatus("Not Started");
            hrTargetTasks.setPriority(1);
            hrTargetTasks.setName(task);
            hrTargetTasks.setDescription(task);
            hrTargetTasks.setProgress(0);
            targetTasksService.insertHrTargetTasks(hrTargetTasks);
        }
    }

    @NotNull
    private static HrEmployees getHrEmployees(HrEmployees employees) {
        HrEmployees hrEmployees1 = new HrEmployees();
        hrEmployees1.setEmployeeId(employees.getEmployeeId());
        hrEmployees1.setResignationAttachment(employees.getResignationAttachment());
        hrEmployees1.setResignationDate(employees.getResignationDate()==null?DateUtils.getNowDate():employees.getResignationDate());
        hrEmployees1.setResignationReason(employees.getResignationReason());
        hrEmployees1.setRehireEligibility(employees.getRehireEligibility());
        hrEmployees1.setSystemAccess(employees.getSystemAccess()==null?"2":employees.getSystemAccess());
        hrEmployees1.setResignationHrUserId(SecurityUtils.getUserId());
        hrEmployees1.setStatus("3");
        return hrEmployees1;
    }

    @Override
    public int restoreEmployees(Long employeeId) {
        HrEmployees hrEmployees = baseMapper.selectById(employeeId);
        Long userId = hrEmployees.getUserId();
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setStatus("0");
        baseMapper.updateSysUser(sysUser);
        HrEmployees hrEmployees1 = new HrEmployees();
        hrEmployees1.setEmployeeId(employeeId);
        hrEmployees1.setStatus("1");
        return baseMapper.updateById(hrEmployees1);
    }

    @Override
    @Transactional
    public int updateAvatarUrl(HrEmployees employees) {
        String avatarUrl = employees.getAvatarUrl();
        Long employeeId = employees.getEmployeeId();
        HrEmployees hrEmployees = selectHrEmployeesById(employeeId);
        SysUser sysUser = new SysUser();
        sysUser.setUserId(hrEmployees.getUserId());
        sysUser.setAvatar(avatarUrl);
        baseMapper.updateSysUser(sysUser);
        return baseMapper.updateById(employees);
    }

    /**
     * Send offboarding reminders to HR and resigned employee one week before resignation date
     * Handles both email notifications and system messages with target task links
     *
     * @param employees Resigned employee information (contains resignation details and user info)
     */
    public void resignEmployeeOneWeekAgo(HrEmployees employees) {
        // Retrieve HR information responsible for handling the offboarding process
        HrEmployees resignationHr = baseMapper.selectHrEmployeesByUserId(employees.getResignationHrUserId());
        // Retrieve enterprise information (used in employee email template)
        HrEnterprise hrEnterprise = candidateInfoMapper.seleEid(employees.getEnterpriseId());
        // Process offboarding reminders for HR (email + system message)
        List<HrTargets> hrOffboardingTargets = getOffboardingTargets(employees.getEmployeeId(), OFFBOARDING_TARGET_HR);
        sendOffboardingReminderToHr(employees, resignationHr, hrOffboardingTargets, "offboardingReminderWeekHr", 20);
        // Process offboarding reminders for resigned employee (email + system message)
        List<HrTargets> staffOffboardingTargets = getOffboardingTargets(employees.getEmployeeId(), OFFBOARDING_TARGET_STAFF);
        sendOffboardingReminderToStaff(employees, hrEnterprise, staffOffboardingTargets, "offboardingReminderWeekEmployee", 22);
}

    @Override
    public void resignEmployeeOneDayAgo(HrEmployees employees) {
        // Retrieve HR information responsible for handling the offboarding process
        HrEmployees resignationHr = baseMapper.selectHrEmployeesByUserId(employees.getResignationHrUserId());
        // Retrieve enterprise information (used in employee email template)
        HrEnterprise hrEnterprise = candidateInfoMapper.seleEid(employees.getEnterpriseId());
        // Process offboarding reminders for HR (email + system message)
        List<HrTargets> hrOffboardingTargets = getOffboardingTargets(employees.getEmployeeId(), OFFBOARDING_TARGET_HR);
        sendOffboardingReminderToHr(employees, resignationHr, hrOffboardingTargets, "offboardingReminderDayHr", 23);
        // Process offboarding reminders for resigned employee (email + system message)
        List<HrTargets> staffOffboardingTargets = getOffboardingTargets(employees.getEmployeeId(), OFFBOARDING_TARGET_STAFF);
        sendOffboardingReminderToStaff(employees, hrEnterprise, staffOffboardingTargets, "offboardingReminderDayEmployee", 24);
    }


    /**
     * Constant definitions - Centralized management for easier maintenance
     */
    // Target task names for offboarding process
    private static final String OFFBOARDING_TARGET_HR = "Complete offboarding - HR";
    private static final String OFFBOARDING_TARGET_STAFF = "Complete offboarding - Staff";
    // HRIS tool name used in email templates
    private static final String HRIS_TOOL_NAME = "Shiftcare HR";
    // Template for offboarding task detail URL (format with target ID)
    private static final String OFFBOARDING_URL_TEMPLATE = "/targets/detail?id=%s";
    // System message status: 0 = Unread
    private static final String MESSAGE_STATUS_UNREAD = "0";

    /**
     * Query offboarding-related target tasks by resigned employee ID and target name
     *
     * @param resignEmployeeId ID of the resigned employee
     * @param targetName       Name of the target task (see OFFBOARDING_TARGET_* constants)
     * @return List of matching offboarding target tasks
     */
    private List<HrTargets> getOffboardingTargets(Long resignEmployeeId, String targetName) {
        QueryWrapper<HrTargets> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("resign_employee_id", resignEmployeeId)
                .eq("name", targetName);
        return targetsService.list(queryWrapper);
    }

    /**
     * Send offboarding reminder to HR (includes email and system message)
     *
     * @param resignedEmployee   Resigned employee information
     * @param resignationHr      HR responsible for offboarding
     * @param offboardingTargets Matching offboarding target tasks
     */
    private void sendOffboardingReminderToHr(HrEmployees resignedEmployee, HrEmployees resignationHr, List<HrTargets> offboardingTargets, String emailType, Integer messageType) {
        // Build email parameters
        Map<String, Object> emailParams = buildHrEmailParams(resignedEmployee, resignationHr, offboardingTargets);
        // Send email notification to HR
        emailUtils.sendEmailByTemplate(emailParams, resignationHr.getEmail(), emailType);
        // Build system message
        SysMessage sysMessage = buildSysMessage(
                resignationHr.getUserId(),
                messageType,
                buildMessageMap1(resignedEmployee.getFullName()),
                buildMessageMap2(offboardingTargets)
        );
        // Send system message to HR
        remoteMessageService.sendMessageByTemplate(sysMessage, SecurityConstants.INNER);
    }

    /**
     * Send offboarding reminder to resigned employee (includes email and system message)
     *
     * @param resignedEmployee   Resigned employee information
     * @param hrEnterprise       Current enterprise information
     * @param offboardingTargets Matching offboarding target tasks
     */
    private void sendOffboardingReminderToStaff(HrEmployees resignedEmployee, HrEnterprise hrEnterprise, List<HrTargets> offboardingTargets, String emailType, Integer messageType) {
        // Build email parameters
        Map<String, Object> emailParams = buildStaffEmailParams(resignedEmployee, hrEnterprise, offboardingTargets);
        // Validate employee email before sending
        Assert.notNull(resignedEmployee.getEmail(), "Resigned employee's email address cannot be null");
        // Send email notification to resigned employee
        emailUtils.sendEmailByTemplate(emailParams, resignedEmployee.getEmail(), emailType);
        Map<String, Object> map1 = new HashMap<>();
        // Build system message
        SysMessage sysMessage = buildSysMessage(
                resignedEmployee.getUserId(),
                messageType,
                map1,
                buildMessageMap2(offboardingTargets)
        );
        // Send system message to resigned employee (fixed bug: used correct sysMessage object)
        remoteMessageService.sendMessageByTemplate(sysMessage, SecurityConstants.INNER);
    }

    /**
     * Build email parameters specifically for HR offboarding reminder
     *
     * @param resignedEmployee   Resigned employee information
     * @param resignationHr      HR responsible for offboarding
     * @param offboardingTargets Matching offboarding target tasks
     * @return Email template parameters map
     */
    private Map<String, Object> buildHrEmailParams(HrEmployees resignedEmployee, HrEmployees resignationHr, List<HrTargets> offboardingTargets) {
        Map<String, Object> params = new HashMap<>();
        params.put("HrFullName", resignationHr.getFullName());
        params.put("FullName", resignedEmployee.getFullName());
        params.put("HrisToolName", HRIS_TOOL_NAME);

        // Add offboarding task URL if targets exist
        if (!offboardingTargets.isEmpty()) {
            params.put("OffboardingUrl", webUrl.getUrl() + String.format(OFFBOARDING_URL_TEMPLATE, offboardingTargets.get(0).getId()));
        }
        return params;
    }

    /**
     * Build email parameters specifically for resigned employee offboarding reminder
     *
     * @param resignedEmployee   Resigned employee information
     * @param hrEnterprise       Current enterprise information
     * @param offboardingTargets Matching offboarding target tasks
     * @return Email template parameters map
     */
    private Map<String, Object> buildStaffEmailParams(HrEmployees resignedEmployee, HrEnterprise hrEnterprise, List<HrTargets> offboardingTargets) {
        Map<String, Object> params = new HashMap<>();
        params.put("FullName", resignedEmployee.getFullName());
        params.put("HrisToolName", HRIS_TOOL_NAME);
        params.put("CompanyName", hrEnterprise.getEnterpriseName());

        // Add offboarding task URL if targets exist (fixed bug: used correct target list)
        if (!offboardingTargets.isEmpty()) {
            params.put("OffboardingUrl",webUrl.getUrl() +  String.format(OFFBOARDING_URL_TEMPLATE, offboardingTargets.get(0).getId()));
        }
        return params;
    }

    /**
     * Build map1 parameter for system message (contains basic employee info)
     *
     * @param fullName Resigned employee's full name
     * @return System message map1
     */
    private Map<String, Object> buildMessageMap1(String fullName) {
        Map<String, Object> map1 = new HashMap<>();
        map1.put("fullName", fullName);
        return map1;
    }

    /**
     * Build map2 parameter for system message (contains target task ID if available)
     *
     * @param targets Matching offboarding target tasks
     * @return System message map2
     */
    private Map<String, Object> buildMessageMap2(List<HrTargets> targets) {
        Map<String, Object> map2 = new HashMap<>();
        if (!targets.isEmpty()) {
            map2.put("id", targets.get(0).getId());
        }
        return map2;
    }

    /**
     * Generic method to build system message object with common properties
     *
     * @param recipientId User ID of the message recipient
     * @param messageType Type of the system message (see MESSAGE_TYPE_* constants)
     * @param map1        Additional message parameters (basic info)
     * @param map2        Additional message parameters (target task info)
     * @return Constructed SysMessage object
     */
    private SysMessage buildSysMessage(Long recipientId, int messageType, Map<String, Object> map1, Map<String, Object> map2) {
        SysMessage sysMessage = new SysMessage();
        sysMessage.setMessageSender(0L);
        sysMessage.setMessageRecipient(recipientId);
        sysMessage.setMessageStatus(MESSAGE_STATUS_UNREAD);
        sysMessage.setMessageType(messageType);
        sysMessage.setCreateTime(DateUtils.getNowDate());
        sysMessage.setMap1(map1);
        sysMessage.setMap2(map2);
        return sysMessage;
    }
}
