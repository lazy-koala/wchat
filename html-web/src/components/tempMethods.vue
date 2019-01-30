getGroupUserList: function (groupList) {
            let that = this;
            let groupId = [];
            for (let i = 0; i < groupList.length; i++) {
                groupId[i] = groupList[i].groupId;
            }
            var params = {
                groupId: groupId
            };
            $axios.get('/api/group/get_group_user_list', {
                params:{
                    groupId: groupId
                },
                paramsSerializer: function(params) {
                    return Qs.stringify(params, {arrayFormat: 'repeat'})
                }
            }
            ).then((res) => {
                // 将数组列表更新到state
                if (res.data.data) {
                    res.data.isInit = true;
                    that.$store.commit('UPDATE_GROUP_FRIENDLIST', res.data);
                }
            });
        },