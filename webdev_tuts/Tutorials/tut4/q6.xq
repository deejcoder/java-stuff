xquery version "3.0";

(:~
: Author: Dylan Tonks (16058989)
: Date: 27/03/2018
: Time: 6:11 PM
: To change this template use File | Settings | File Templates.
:)

for $x in doc("xml/MemberData.xml")/MemberData/members/member
order by $x/member-info/lastname
where $x/member-info/state/state-fullname eq "Arizona"
return $x