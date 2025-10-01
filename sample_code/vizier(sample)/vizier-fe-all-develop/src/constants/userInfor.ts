const currentUserInfor = JSON.parse(localStorage.getItem("userInfo") || "{}");

export const userInfor = {
    userId: currentUserInfor?.userId || null,
    userNm: currentUserInfor?.orgNm || null
}

export const getUserInfor = () => {
    const currentUserInfor = JSON.parse(localStorage.getItem("userInfo") || "{}");
    const userInfor = {
        chgUser: currentUserInfor?.userId || null,
        chgDeptName: currentUserInfor?.orgNm || null
    }
    return userInfor
}
